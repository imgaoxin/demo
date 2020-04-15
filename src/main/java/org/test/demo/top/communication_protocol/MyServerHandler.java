package org.test.demo.top.communication_protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author gx
 * @create 2019-08-20 16:31
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    //private static volatile AtomicInteger c = new AtomicInteger(0);
    private long start;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("client link " + ctx.channel().remoteAddress());
        start = System.currentTimeMillis();

        //CompletableFuture.runAsync(() -> {
        for (int i = 0; i < 100000; i++) {
            ctx.channel().write(new Message(2, "您这，嘛去？"));
            ctx.channel().write(new Message(3, "有空家里坐坐啊。"));
            if (i % 50 == 0){
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
            // 收到问题q1
            ctx.writeAndFlush(new Message(1, "刚吃。"));
        } else if (id == 2) {
            // 收到回复a2
        } else if (id == 3) {
            // 收到回复a3
            //if (c.incrementAndGet() >= 100000)
            //    System.out.println("收到回复a3完成用时 " + (System.currentTimeMillis() - start) / 1000.0);
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