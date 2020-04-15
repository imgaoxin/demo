package org.test.demo.top.simple_rpc_framework.rpc_netty.client;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestIdSupport{
    private final static AtomicInteger nextRequestId = new AtomicInteger(0);

    public static int next(){
        return nextRequestId.getAndIncrement();
    }
}