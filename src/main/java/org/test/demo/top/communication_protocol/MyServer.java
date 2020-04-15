package org.test.demo.top.communication_protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * @author gx
 * @create 2019-08-20 16:15
 */
public class MyServer {

    private CountDownLatch countDownLatch;

    public MyServer(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    //"Lidaye"
    public void start(String localHostname, int localPort) {
        //创建一组线程
        EventLoopGroup group1 = new NioEventLoopGroup(2);
        //EventLoopGroup group2 = new NioEventLoopGroup();
        try {
            //初始化 server
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group1/*, group2*/)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(localHostname, localPort));
            //设置收到数据后的处理 handler
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new MyDecoder());
                    ch.pipeline().addLast(new MyEncoder());
                    ch.pipeline().addLast(new MyServerHandler());
                }
            });
            //绑定端口，开始提供服务
            ChannelFuture channelFuture = serverBootstrap.bind();
            countDownLatch.countDown();
            channelFuture.sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                group1.shutdownGracefully().sync();
                //group2.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
