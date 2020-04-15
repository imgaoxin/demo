package org.test.demo.top.communication_protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

/**
 * @author gx
 * @create 2019-08-23 12:21
 */
public class MyDecoder extends LengthFieldBasedFrameDecoder {
    private static final int maxFrameLength = 1024 * 1024;
    private static final int lengthFieldOffset = 0;
    // length is int, int = 4 bytes
    private static final int lengthFieldLength = 4;

    public MyDecoder() {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        long id;
        String value;
        ByteBuf buf = null;
        try {
            buf = (ByteBuf) super.decode(ctx, in);
            if (buf == null) {
                return null;
            }
            int len = buf.readInt();
            id = buf.readLong();
            byte[] bytes = new byte[len - Message.ID_WIDTH];
            buf.readBytes(bytes);
            value = new String(bytes, CharsetUtil.UTF_8);
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
        return new Message(id, value);
    }
}
