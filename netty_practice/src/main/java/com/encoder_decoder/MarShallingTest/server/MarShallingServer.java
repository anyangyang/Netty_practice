package com.encoder_decoder.MarShallingTest.server;



import com.encoder_decoder.MarShallingTest.MarShallingCodeFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;



/**
 * Created by zhujiating on 2017/7/17.
 */
public class MarShallingServer {
    private final int port;
    MarShallingServer(int port){
        this.port = port;
    }
    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap sbs = new ServerBootstrap();

        sbs.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,1024).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                // 在管道任务中添加 MarShalling 的解码器和编码器
                socketChannel.pipeline().addLast(MarShallingCodeFactory.buildMarshallingDecoder());
                socketChannel.pipeline().addLast(MarShallingCodeFactory.buildMarshallingEncoder());
                socketChannel.pipeline().addLast(new MarshallingHandler());

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
        MarShallingServer server = new MarShallingServer(8080);
        server.run();
    }


}
