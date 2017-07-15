package com.DelimiterDecoder.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zhujiating on 2017/7/6.
 */
public class EchoServerHandler extends ChannelHandlerAdapter{

    int counter = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接成功");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String clientRequest = (String)msg;
        System.out.println("this is"+ ++counter +" server receive order:"+msg);

        // 向客户端发送
        String serverReply = "hello client"+"$_";
        ByteBuf bytebuf = Unpooled.copiedBuffer(serverReply.getBytes());
        ctx.writeAndFlush(bytebuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
