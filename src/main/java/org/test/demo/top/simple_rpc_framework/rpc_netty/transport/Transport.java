package org.test.demo.top.simple_rpc_framework.rpc_netty.transport;

import java.util.concurrent.CompletableFuture;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Command;

public interface Transport{
    /**
     * 发送请求指令
     * @param request 请求指令
     * @return 返回值是一个Future
     */
    CompletableFuture<Command> send(Command request);
}