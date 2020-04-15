package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.netty;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.InFlightRequests;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.ResponseFuture;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseInvocation.class);
    private final InFlightRequests inFlightRequests;

    ResponseInvocation(InFlightRequests inFlightRequests) {
        this.inFlightRequests = inFlightRequests;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command msg) throws Exception {
        ResponseFuture future = inFlightRequests.remove(msg.getHeader().getRequestId());
        if (future != null) {
            // 成功获取到响应，设置到future
            future.getFuture().complete(msg);
        }else{
            logger.warn("Drop response: {}", msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Exception: ", cause);
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive())ctx.close();
    }
}