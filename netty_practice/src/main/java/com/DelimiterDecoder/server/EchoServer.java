package com.DelimiterDecoder.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by zhujiating on 2017/7/5.
 */
public class EchoServer {

    public void bind(int port){
        // 首先创建两个工作组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        // 创建一个服务端启动辅助类 ServerBootstarp
         ServerBootstrap sbs = new ServerBootstrap();

        /** 1、绑定工作组
         *  2、指定通道: NioServerSocketChannel.class
         *  3、设置tcp参数
         *  4、指定handler
         */
        sbs.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,1024).childHandler(new ChannelInitializer<SocketChannel>(){
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //  约定特殊符号，作为包结尾，
                ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new EchoServerHandler());
            }
        });
        try{
            // 绑定端口
            ChannelFuture cf = sbs.bind(port).sync();

            cf.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
            // 优雅地关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args){
        EchoServer  es = new EchoServer();
        es.bind(8080);
    }
}
