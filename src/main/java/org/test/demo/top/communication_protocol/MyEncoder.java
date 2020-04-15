package org.test.demo.top.communication_protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author gx
 * @create 2019-08-23 12:17
 */
public class MyEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        try {
            byte[] value = msg.getValue().getBytes(CharsetUtil.UTF_8);
            // length = byteLen(body) + byteLen(id)
            // id is long, long = 8 bytes
            out.writeInt(value.length + Message.ID_WIDTH);
            out.writeLong(msg.getId());
            out.writeBytes(value);
        } finally {
            // nothing
        }
    }
}
