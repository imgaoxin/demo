package org.test.demo.top.simple_rpc_framework.server;

import org.test.demo.top.simple_rpc_framework.service_api.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }

}