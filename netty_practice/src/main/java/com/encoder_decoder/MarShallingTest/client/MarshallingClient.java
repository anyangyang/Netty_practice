package com.encoder_decoder.MarShallingTest.client;

import com.encoder_decoder.MarShallingTest.MarShallingCodeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by zhujiating on 2017/7/19.
 */
public class MarshallingClient {
    private final String host;
    private final int port;
    private final int sendNumber;
    MarshallingClient(String host,int port,int sendNumber){
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void connect(){
        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();
        bs.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(MarShallingCodeFactory.buildMarshallingDecoder());
                socketChannel.pipeline().addLast(MarShallingCodeFactory.buildMarshallingEncoder());
                socketChannel.pipeline().addLast(new MarshallingClientHandler(sendNumber));
            }
        });
        try{
            ChannelFuture cf = bs.connect(host,port).sync();
            cf.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args){
        MarshallingClient marshallingClient = new MarshallingClient("localhost",8080,5);
        marshallingClient.connect();
    }

}
