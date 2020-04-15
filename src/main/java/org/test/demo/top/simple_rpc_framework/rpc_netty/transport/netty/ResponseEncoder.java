package org.test.demo.top.simple_rpc_framework.rpc_netty.transport.netty;

import java.nio.charset.StandardCharsets;

import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.Header;
import org.test.demo.top.simple_rpc_framework.rpc_netty.transport.command.ResponseHeader;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ResponseEncoder extends CommandEncoder {
    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(channelHandlerContext, header, byteBuf);
        if(header instanceof ResponseHeader) {
            ResponseHeader responseHeader = (ResponseHeader) header;
            byteBuf.writeInt(responseHeader.getCode());
            int errorLength = header.length() - (Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                    Integer.BYTES);
            byteBuf.writeInt(errorLength);
            byteBuf.writeBytes(responseHeader.getError() == null ? new byte[0]: responseHeader.getError().getBytes(StandardCharsets.UTF_8));
        } else {
            throw new Exception(String.format("Invalid header type: %s!", header.getClass().getCanonicalName()));
        }
    }
}