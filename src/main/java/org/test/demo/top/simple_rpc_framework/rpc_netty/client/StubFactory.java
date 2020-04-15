package org.test.demo.top.simple_rpc_framework.rpc_netty.client;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.Transport;

public interface StubFactory{
    /**
     * 创建代理对象
     * @param <T> 服务接口类型
     * @param transport 向服务端发送请求
     * @param serviceClass 服务接口类型(代理对象类型)的class对象
     * @return 代理对象(桩)
     */
    <T> T createStub(Transport transport, Class<T> serviceClass);
}