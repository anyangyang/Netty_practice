package com.FixedLengthDecoder;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by zhujiating on 2017/7/8.
 */
public class EchoServer {

    // bind 方法
    public void bind(int port){
        // 创建两个工作组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        // 创建一个启动辅助类
        final ServerBootstrap sbs = new ServerBootstrap();

        /**
         * 1、绑定工作组
         * 2、指定通道
         * 3、设置tcp参数
         * 4、指定handler
         */
        sbs.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,1024).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                // 创建管道任务
                socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(20));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new EchoServerHandler());
            }
        });
        try{
            // 绑定监听端口
            ChannelFuture cf = sbs.bind(port).sync();
            cf.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args){
        EchoServer es = new EchoServer();
        es.bind(8080);
    }
}
