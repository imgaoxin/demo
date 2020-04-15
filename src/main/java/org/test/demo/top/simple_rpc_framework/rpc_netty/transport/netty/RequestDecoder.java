package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.netty;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Header;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class RequestDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        return new Header(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
    }
}