package com.DelimiterDecoder.client;

import io.netty.bootstrap.Bootstrap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by zhujiating on 2017/7/6.
 */
public class EchoClient {

   public void connect(String host,int port){
       // 创建一个工作组
       EventLoopGroup workGroup = new NioEventLoopGroup();
       Bootstrap bs = new Bootstrap();

       bs.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).handler(new ChannelInitializer<SocketChannel>() {
           @Override
           protected void initChannel(SocketChannel socketChannel) throws Exception {
               //  管道任务，管道中有阀
               ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
               socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
               socketChannel.pipeline().addLast(new StringDecoder());
               socketChannel.pipeline().addLast(new EchoClientHandler());
           }
       });

       try{
           //连接客户端
           ChannelFuture cf = bs.connect(host,port).sync();
           cf.channel().closeFuture().sync();
       }catch(InterruptedException e){
           e.printStackTrace();
           // 优雅地关闭
           workGroup.shutdownGracefully();
       }

   }

    public static void main(String[] args){
        EchoClient ec =new EchoClient();
        ec.connect("localhost",8080);
    }




}
