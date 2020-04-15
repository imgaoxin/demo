package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.netty;

import java.util.concurrent.CompletableFuture;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.InFlightRequests;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.ResponseFuture;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.Transport;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Command;

import io.netty.channel.Channel;

public class NettyTransport implements Transport {
    private final Channel channel;
    private final InFlightRequests inFlightRequests;

    NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        // 构建返回值
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            // 将在途请求放到inFlightRequests中
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));
            // 发送
            channel.writeAndFlush(request).addListener(future -> {
                // 处理发送失败
                if (!future.isSuccess()) {
                    completableFuture.completeExceptionally(future.cause());
                    channel.close();
                }
            });
        } catch (Throwable t) {
            // 处理发送异常
            // 删除发送异常的在途请求
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }
        return completableFuture;
    }
}