package org.test.demo.top.communication_protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author gx
 * @create 2019-08-21 18:01
 */
public class MyClientHandler extends ChannelInboundHandlerAdapter {
    //private static volatile AtomicInteger c = new AtomicInteger(0);
    private long start;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
        System.out.println("link to server " + ctx.channel().remoteAddress());
        start = System.currentTimeMillis();

        //CompletableFuture.runAsync(() -> {
        for (int i = 0; i < 100000; i++) {
            ctx.channel().write(new Message(1, "吃了没？您呐？"));
            if (i % 100 == 0){
                ctx.channel().flush();
            }
        }
        ctx.channel().write(new Message(4, "end"));
        ctx.channel().flush();
        //});
        //ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message in = (Message) msg;
        long id = in.getId();
        if (id == 1) {
            // 收到回复a1
            //if (c.incrementAndGet() >= 100000)
            //    System.out.println("收到回复a1完成用时 " + (System.currentTimeMillis() - start) / 1000.0);
        } else if (id == 2) {
            // 收到问题q2
            ctx.writeAndFlush(new Message(2, "嗨，没事儿溜溜弯儿。"));
        } else if (id == 3) {
            // 收到问题q3
            ctx.writeAndFlush(new Message(3, "回头去给老太太请安！"));
        } else {
            // 未知消息
            System.out.println("收到回复完成用时 " + (System.currentTimeMillis() - start) / 1000.0);
        }
        //ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
