package org.test.demo.top.simple_rpc_framework.server;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

import org.test.demo.top.simple_rpc_framework.rpc_api.NameService;
import org.test.demo.top.simple_rpc_framework.rpc_api.RpcAccessPoint;
import org.test.demo.top.simple_rpc_framework.rpc_api.spi.ServiceSupport;
import org.test.demo.top.simple_rpc_framework.service_api.HelloService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        // 获取NameService实例的URI
        File file = new File(System.getProperty("java.io.tmpdir"), "simple_rpc_framework_nameservice.data");
        URI uri = file.toURI();
        
        // 启动服务端，注册服务
        String serviceName = HelloService.class.getCanonicalName();
        HelloService helloService = new HelloServiceImpl();

        logger.info("创建并启动RpcAccessPoint...");
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
                Closeable ignore = rpcAccessPoint.startServer()) {

            NameService nameService = rpcAccessPoint.getNameService(uri);
            assert nameService != null;

            logger.info("向RpcAccessPoint注册{}服务...", serviceName);
            uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);

            logger.info("服务名: {}, URI: {}, 向NameService注册...", serviceName, uri);
            nameService.registerService(serviceName, uri);

            logger.info("开始提供服务，按任何键退出.");
            // no inspection ResultOfMethodCallIgnored
            System.in.read();
            
            logger.info("Bye!");
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
        }
    }
}