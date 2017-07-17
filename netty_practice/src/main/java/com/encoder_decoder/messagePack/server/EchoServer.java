package com.encoder_decoder.messagePack.server;


import com.encoder_decoder.messagePack.messagePackDecode.MsgPackDecoder;
import com.encoder_decoder.messagePack.messagePackEncode.MsgPackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by zhujiating on 2017/7/15.
 */
public class EchoServer {
    final int port;
    EchoServer(int port){
        this.port = port;
    }
    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap sbs = new ServerBootstrap();

        sbs.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,1024).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1024,0,2,0,2));
                socketChannel.pipeline().addLast("decoder",new MsgPackDecoder());
                socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
                socketChannel.pipeline().addLast("encoder",new MsgPackEncoder());
                socketChannel.pipeline().addLast(new EchoServerHandler());
            }
        });
        try{
            ChannelFuture cf = sbs.bind(port).sync();
            cf.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }


    }

    public static void main(String[] args){
        EchoServer server = new EchoServer(8080);
        server.run();
    }
}
