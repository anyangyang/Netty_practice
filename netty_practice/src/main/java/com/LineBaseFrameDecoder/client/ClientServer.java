package com.LineBaseFrameDecoder.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by zhujiating on 2017/7/3.
 * 用于练习tcp粘包/拆包
 */
public class ClientServer {

    public void connect(String host,int port) {
        // 首先创建一个线程组，
        EventLoopGroup workGroup = new NioEventLoopGroup();
        // 创建一个启动对象
        Bootstrap bs = new Bootstrap();
        /**
         * 1、传入工作组
         * 2、指定通道
         * 3、设置 tcp 参数
         * 4、设置 IO 处理类
         */
        bs.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                socketChannel.pipeline().addLast(new StringDecoder());
                socketChannel.pipeline().addLast(new TimeClientHandler());
            }
        });


        try{
            // 同步连接服务端
            ChannelFuture cf = bs.connect(host,port).sync();
            // 等待客户端链路关闭
            cf.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args){
        ClientServer cs =new ClientServer();
        cs.connect("localhost",8080);
    }
}
