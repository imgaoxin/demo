package org.test.demo.top.simple_rpc_framework.rpc_netty;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import org.test.demo.top.simple_rpc_framework.rpc_api.RpcAccessPoint;
import org.test.demo.top.simple_rpc_framework.rpc_api.spi.ServiceSupport;
import org.test.demo.top.simple_rpc_framework.rpc_netty.client.StubFactory;
import org.test.demo.top.simple_rpc_framework.rpc_netty.server.ServiceProviderRegistry;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.RequestHandlerRegistry;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.Transport;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.TransportClient;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.TransportServer;

public class NettyRpcAccessPoint implements RpcAccessPoint {
    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);
    private TransportServer server = null;
    private TransportClient client = ServiceSupport.load(TransportClient.class);
    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();
    private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);
    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()), 30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return uri;
    }

    @Override
    public Closeable startServer() throws Exception {
        if (server == null) {
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(), port);
        }
        return () -> {
            if (server != null) {
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (server != null) {
            server.stop();
        }
        client.close();
    }
}