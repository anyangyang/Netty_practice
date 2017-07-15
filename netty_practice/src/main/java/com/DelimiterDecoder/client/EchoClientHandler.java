package com.DelimiterDecoder.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zhujiating on 2017/7/6.
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    int counter = 0;
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接成功 ，向客户端发送数据
        String clientOrder = "hello server"+"$_";
        byte[] bytes = clientOrder.getBytes();
        for(int i = 0;i<100;i++){
            ByteBuf byteBuf = Unpooled.buffer(bytes.length);
            byteBuf.writeBytes(bytes);
            ctx.writeAndFlush(byteBuf);
        }


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String serverReply = (String)msg;
        System.out.println("this is "+ ++counter+" client receive reply "+serverReply);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
    }
}
