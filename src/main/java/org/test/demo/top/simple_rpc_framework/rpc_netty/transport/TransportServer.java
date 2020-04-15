package org.test.demo.top.simple_rpc_framework.rpc_netty.transport;

public interface TransportServer {
    void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception;

    void stop();
}