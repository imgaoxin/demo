package org.test.demo.top.simple_rpc_framework.client;

import java.io.File;
import java.net.URI;

import org.test.demo.top.simple_rpc_framework.rpc_api.NameService;
import org.test.demo.top.simple_rpc_framework.rpc_api.RpcAccessPoint;
import org.test.demo.top.simple_rpc_framework.rpc_api.spi.ServiceSupport;
import org.test.demo.top.simple_rpc_framework.service_api.HelloService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        // 获取NameService实例的URI
        File file = new File(System.getProperty("java.io.tmpdir"), "simple_rpc_framework_nameservice.data");
        URI uri = file.toURI();

        // 通过rpc远程调用服务
        String serviceName = HelloService.class.getCanonicalName();

        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            NameService nameService = rpcAccessPoint.getNameService(uri);
            assert nameService != null;

            uri = nameService.lookupService(serviceName);
            assert uri != null;

            logger.info("找到服务{}，提供者: {}.", serviceName, uri);
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);

            logger.info("请求服务, name: {}...", serviceName);
            String response = helloService.hello("world");
            logger.info("收到响应: {}.", response);

        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }
}