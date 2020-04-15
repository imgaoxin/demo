package org.test.demo.top.simple_rpc_framework.rpc_netty.nameservice;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.test.demo.top.simple_rpc_framework.rpc_api.NameService;
import org.test.demo.top.simple_rpc_framework.rpc_netty.serialize.SerializeSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于本地文件实现的注册中心（单机版） 由于本地文件是一个共享资源（被RPC框架的所有客户端和服务端并发读写），需要加文件锁
 */
public class LocalFileNameService implements NameService {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileNameService.class);
    private static final Collection<String> schemes = Collections.singleton("file");
    private File file;

    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {
        if (schemes.contains(nameServiceUri.getScheme())) {
            file = new File(nameServiceUri);
        } else {
            throw new RuntimeException("Unsupported scheme!");
        }
    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {
        logger.info("Register service: {}, uri: {}.", serviceName, uri);

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
                FileChannel fileChannel = raf.getChannel();
                FileLock lock = fileChannel.lock()) {

            Metadata metadata = getMetadata(raf, fileChannel);

            List<URI> uris = metadata.computeIfAbsent(serviceName, e -> new ArrayList<>());
            if (!uris.contains(uri)) {
                uris.add(uri);
            }

            logger.info(metadata.toString());

            byte[] bytes = SerializeSupport.serialize(metadata);
            fileChannel.truncate(bytes.length);
            fileChannel.position(0L);
            fileChannel.write(ByteBuffer.wrap(bytes));
            fileChannel.force(true);
        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        Metadata metadata;
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
                FileChannel fileChannel = raf.getChannel();
                FileLock lock = fileChannel.lock()) {

            metadata = getMetadata(raf, fileChannel);
        }

        logger.info(metadata.toString());

        List<URI> uris = metadata.get(serviceName);
        if (null == uris || uris.isEmpty()) {
            return null;
        } else {
            return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
        }
    }

    private Metadata getMetadata(RandomAccessFile raf, FileChannel fileChannel) throws IOException {
        Metadata metadata;
        int fileLength = (int) raf.length();
        if (fileLength > 0) {
            byte[] bytes = new byte[fileLength];
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            while (buffer.hasRemaining()) {
                fileChannel.read(buffer);
            }
            metadata = SerializeSupport.parse(bytes);
        } else {
            metadata = new Metadata();
        }
        return metadata;
    }
}