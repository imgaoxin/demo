package org.test.demo.top.simple_rpc_framework.rpc_netty.transport;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

public interface TransportClient extends Closeable {
    Transport createTransport(SocketAddress address, long connectionTimeout)
            throws InterruptedException, TimeoutException;
}