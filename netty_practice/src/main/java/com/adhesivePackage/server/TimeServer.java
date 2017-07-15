package com.adhesivePackage.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by zhujiating on 2017/7/3.
 * 用于练习tcp粘包/拆包
 */
public class TimeServer {

    public void bind(int port){
        /**首先创建两个线程组：
         * 第一个 NioEventLoopGroup 用于服务端接收客户端的连接
         * 第二个 NioEventloopGroup 用于处理 SockChannel 的读写
         */

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        // 创建一个 ServerBootstrap ：这个一个 netty 用于启动 Nio 服务点的辅助启动类，可以降低服务端的开发难度
        ServerBootstrap sb = new ServerBootstrap();
        /**把两个线程组传到ServerBootstrap,接着设置创建的 channel 为 NioSersockChannel.class
         * 然后设置 tcp 参数:
         * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
         * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，
         * Java将使用默认值50
         * 最后绑定IO事件的处理类
         */
        sb.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,1024).childHandler(new ChildChannelHandler());


        try{
            // 绑定端口,同步等待成功
            ChannelFuture cf = sb.bind(port).sync();

            // 等待服务端监听端口关闭
            cf.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            // 优雅地关闭线程组
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }




    }


     public static void main(String[] args){
        TimeServer ts = new TimeServer();
        ts.bind(8080);
     }
}
