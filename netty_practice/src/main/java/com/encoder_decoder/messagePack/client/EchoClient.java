package com.encoder_decoder.messagePack.client;

import com.encoder_decoder.messagePack.messagePackDecode.MsgPackDecoder;
import com.encoder_decoder.messagePack.messagePackEncode.MsgPackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.io.IOException;

/**
 * Created by zhujiating on 2017/7/15.
 */
public class EchoClient {
     private final String host;
     private final int port;
     private final int sendNumber;

     EchoClient(String host,int port,int sendNumber){
         this.host = host;
         this.port = port;
         this.sendNumber = sendNumber;
     }

     public void run(){

         EventLoopGroup workGroup = new NioEventLoopGroup();
         Bootstrap bs = new Bootstrap();

         bs.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).handler(new ChannelInitializer<SocketChannel>() {

             @Override
             protected void initChannel(SocketChannel socketChannel) throws Exception {
                 // 在这里增加自己写的解码器和编码器
                 socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1024,0,2,0,2));
                 socketChannel.pipeline().addLast("decoder",new MsgPackDecoder());
                 socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
                 socketChannel.pipeline().addLast("encoder",new MsgPackEncoder());
                 socketChannel.pipeline().addLast(new EchoClientHandler(sendNumber));
             }
         });
         try{
             ChannelFuture cf = bs.connect(host,port).sync();
             cf.channel().closeFuture().sync();
         }catch(InterruptedException e){
             e.printStackTrace();
             workGroup.shutdownGracefully();
         }

     }

     public static void main(String[] args){
         EchoClient client = new EchoClient("localhost",8080,1000);
         client.run();
     }

}
