package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.netty;

import java.nio.charset.StandardCharsets;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Header;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.ResponseHeader;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ResponseDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        int type = byteBuf.readInt();
        int version = byteBuf.readInt();
        int requestId = byteBuf.readInt();
        int code = byteBuf.readInt();
        int errorLength = byteBuf.readInt();
        byte[] errorBytes = new byte[errorLength];
        byteBuf.readBytes(errorBytes);
        String error = new String(errorBytes, StandardCharsets.UTF_8);
        return new ResponseHeader(type, version, requestId, code, error);
    }
}