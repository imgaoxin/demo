package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.netty;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class RequestEncoder extends CommandEncoder {
    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(channelHandlerContext, header, byteBuf);
    }
}