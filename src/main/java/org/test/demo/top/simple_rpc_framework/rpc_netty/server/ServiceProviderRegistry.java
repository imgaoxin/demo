package org.test.demo.top.simple_rpc_framework.rpc_netty.server;

public interface ServiceProviderRegistry {
    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);
}