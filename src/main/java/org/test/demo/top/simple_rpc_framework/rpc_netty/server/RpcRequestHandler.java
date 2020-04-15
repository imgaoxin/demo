package org.test.demo.top.simple_rpc_framework.rpc_netty.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.test.demo.top.simple_rpc_framework.rpc_api.spi.Singleton;
import org.test.demo.top.simple_rpc_framework.rpc_netty.client.ServiceTypes;
import org.test.demo.top.simple_rpc_framework.rpc_netty.client.ServiceVersions;
import org.test.demo.top.simple_rpc_framework.rpc_netty.client.stubs.RpcRequest;
import org.test.demo.top.simple_rpc_framework.rpc_netty.serialize.SerializeSupport;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.RequestHandler;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Code;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Command;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Header;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.ResponseHeader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    private Map<String/* service name */, Object/* service provider */> serviceProviders = new HashMap<>();

    @Override
    public Command handle(Command request) {
        Header header = request.getHeader();
        // 针对请求版本的处理逻辑
        if (header.getVersion() != ServiceVersions.VERSION_RPC_REQUEST) {
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.NO_PROVIDER.getCode(), "No match version!"), new byte[0]); 
        }
        // 从payload中反序列化RpcRequest
        RpcRequest rpcRequest = SerializeSupport.parse(request.getPayload());

        try {
            // 查找需要调用的已注册的服务提供者
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider != null) {
                // 找到服务提供者，利用Java反射机制调用服务的对应方法
                String arg = SerializeSupport.parse(rpcRequest.getSerializedArguments());
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), String.class);
                String result = (String) method.invoke(serviceProvider, arg);
                // 把结果封装成响应命令并返回
                return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId()),
                        SerializeSupport.serialize(result));
            }
            // 如果没找到，返回NO_PROVIDER错误响应。
            logger.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.NO_PROVIDER.getCode(), Code.NO_PROVIDER.getMessage()), new byte[0]); 
        } catch (Exception e) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            logger.warn("Exception: ", e);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.UNKNOWN_ERROR.getCode(), e.getMessage()), new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }

    @Override
    public synchronized <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        logger.info("Add service: {}, provider: {}.", serviceClass.getCanonicalName(),
                serviceProvider.getClass().getCanonicalName());
    }
}