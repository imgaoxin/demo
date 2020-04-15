package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.netty;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.RequestHandler;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.RequestHandlerRegistry;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {
    private static final Logger logger = LoggerFactory.getLogger(RequestInvocation.class);
    private final RequestHandlerRegistry requestHandlerRegistry;

    public RequestInvocation(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command msg) throws Exception {
        /**
         * 根据请求命令的 Header 中的请求类型 type， 去 requestHandlerRegistry 中查找对应的请求处理器
         * RequestHandler， 然后调用请求处理器去处理请求，最后把结果发送给客户端。
         */
        RequestHandler handler = requestHandlerRegistry.get(msg.getHeader().getType());
        if (null != handler) {
            Command response = handler.handle(msg);
            if (null != response) {
                ctx.writeAndFlush(response).addListener(channelFuture -> {
                    if (!channelFuture.isSuccess()) {
                        logger.warn("Write response failed!", channelFuture.cause());
                        ctx.channel().close();
                    }
                });
            } else {
                logger.warn("Response is null!");
            }
        } else {
            throw new Exception(String.format("No handler for request with type: %d!", msg.getHeader().getType()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Exception: ", cause);
        super.exceptionCaught(ctx, cause);

        Channel channel = ctx.channel();
        if (channel.isActive())
            ctx.close();
    }
}