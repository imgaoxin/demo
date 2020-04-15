package org.test.demo.base.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author gx
 * @create 2019-09-16 14:47
 */
public class NIOTest {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                server();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                client();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                client();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static void server() throws IOException {
        // 多路复用器
        Selector selector;
        ByteBuffer serverRead = ByteBuffer.allocate(64);
        ByteBuffer serverWrite = ByteBuffer.allocate(64);

        selector = Selector.open();

        // 开启服务器通道
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定启动端口
        serverSocketChannel.bind(new InetSocketAddress("localhost", 9222));
        // 注册，标记服务通道状态
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 阻塞方法
            selector.select();
            // 返回选中的通道标记集合
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                // 删除本次处理的key
                keys.remove();
                // 通道是否有效
                if (key.isValid()) {
                    try {
                        // 连接成功
                        if (key.isAcceptable()) {
                            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                            // 阻塞方法。客户端发起请求后返回
                            SocketChannel socket = channel.accept();
                            socket.configureBlocking(false);

                            // 注册读数据通道状态
                            socket.register(selector, SelectionKey.OP_READ);
                        }
                        // 读数据
                        if (key.isReadable()) {
                            serverRead.clear();
                            SocketChannel socket = (SocketChannel) key.channel();
                            // 读取通道数据到缓存中
                            int redLen = socket.read(serverRead);
                            if (redLen == -1) {
                                // 关闭通道
                                socket.close();
                                // 关闭连接
                                key.cancel();
                            } else {
                                // 重置缓存游标
                                serverRead.flip();
                                byte[] temp = new byte[serverRead.remaining()];
                                serverRead.get(temp);
                                System.out
                                        .println("from" + socket.getRemoteAddress() + ":" + new String(temp, "UTF-8"));

                                // 注册写操作
                                socket.register(selector, SelectionKey.OP_WRITE);
                            }
                        }
                        // 写数据
                        if (key.isWritable()) {
                            serverWrite.clear();
                            SocketChannel socket = (SocketChannel) key.channel();
                            serverWrite.put("hello client ...".getBytes("UTF-8"));
                            serverWrite.flip();
                            socket.write(serverWrite);

                            // 注册读操作
                            socket.register(selector, SelectionKey.OP_READ);
                        }
                    } catch (Exception e) {
                        // 断开连接
                        key.cancel();
                    }
                }
            }
        }
    }

    static void client() throws IOException {
        SocketChannel socket = null;
        try {
            ByteBuffer buff = ByteBuffer.allocate(64);
            // 开启通道
            socket = SocketChannel.open();
            // 连接服务器
            socket.connect(new InetSocketAddress("localhost", 9222));

            buff.put("hello server ...".getBytes("UTF-8"));
            buff.flip();
            socket.write(buff);
            buff.clear();

            socket.read(buff);
            buff.flip();
            byte[] temp1 = new byte[buff.remaining()];
            buff.get(temp1);
            System.out.println("from server" + socket.getRemoteAddress() + ":" + new String(temp1, "UTF-8"));
            buff.clear();

            buff.put("bye server ...".getBytes("UTF-8"));
            buff.flip();
            socket.write(buff);
            buff.clear();
            
            socket.read(buff);
            buff.flip();
            byte[] temp2 = new byte[buff.remaining()];
            buff.get(temp2);
            System.out.println("from server" + socket.getRemoteAddress() + ":" + new String(temp2, "UTF-8"));
            buff.clear();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
