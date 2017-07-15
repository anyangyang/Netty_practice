package com.adhesivePackage.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zhujiating on 2017/7/3.
 */
public class TimeClientHandler extends ChannelHandlerAdapter{
    // 用于统计客户端发回来的消息数
    private int counter;
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接成功，那么就要发送一些数据
        String clientMsg = "hello server!"+System.getProperty("line.separator");
        byte[] bytes = clientMsg.getBytes();
        // 给客户端发送 100 条信息
        for(int i=0;i<100;i++){
            ByteBuf buffer = Unpooled.buffer(bytes.length);
            buffer.writeBytes(bytes);
            ctx.writeAndFlush(buffer);
        }


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       ByteBuf buf = (ByteBuf)msg;
       byte[] bytes = new byte[buf.readableBytes()];
       buf.readBytes(bytes);
       String serverResponse = new String(bytes,"UTF-8");
       System.out.println("client receive response:"+serverResponse+"; the counter is:"+ ++counter);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
