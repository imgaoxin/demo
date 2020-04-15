package org.test.demo.top.simple_rpc_framework.rpc_api;

import java.io.Closeable;
import java.net.URI;
import java.util.Collection;

import org.test.demo.top.simple_rpc_framework.rpc_api.spi.ServiceSupport;

/**
 * Rpc框架对外提供的服务接口
 */
public interface RpcAccessPoint extends Closeable{

    /**
     * 客户端获取远程服务的引用
     * @param <T> 服务接口的类型
     * @param uri 远程服务地址
     * @param serviceClass 服务接口类的Class
     * @return 远程服务引用
     */
    <T> T getRemoteService(URI uri, Class<T> serviceClass);

    /**
     * 服务端注册服务的实例
     * @param <T> 服务接口的类型
     * @param service 服务的实现的实例
     * @param serviceClass 服务接口类的Class
     * @return 服务地址
     */
    <T> URI addServiceProvider(T service, Class<T> serviceClass);

    /**
     * 获取注册中心的引用
     * @param nameServiceUri 注册中心URI
     * @return 注册中心的引用
     */
    default NameService getNameService(URI nameServiceUri){
        Collection<NameService> nameServices = ServiceSupport.loadAll(NameService.class);
        for (NameService nameService : nameServices) {
            // 支持此协议，连接注册中心并返回
            if(nameService.supportedSchemes().contains(nameServiceUri.getScheme())){
                nameService.connect(nameServiceUri);
                return nameService;
            }
        }
        return null;
    }

    /**
     * 服务端启动Rpc框架，监听接口，开始提供远程服务。
     * @return Rpc服务的实例，用于程序停止时安全关闭服务
     * @throws Exception
     */
    Closeable startServer() throws Exception;
}