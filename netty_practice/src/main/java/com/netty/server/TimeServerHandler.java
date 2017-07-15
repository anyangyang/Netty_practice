package com.netty.server;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;

/**
 *Created by zhujiating on 2017/7/3.
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接成功！");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /** 到这个方法的时候，客户端发送过来的数据，已经传入到 Object 中了，我们需要将 msg 对象向下转型成 ByteBuf(netty中的bytebuffer）
         *  netty 中的 ByteBuf 和 NIO 中 ByteBuffer 中 get、put 方法不一样，需要用到 readByte 和 WriteByte
         */
        ByteBuf buffer = (ByteBuf)msg;
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        String clientMsg =  new String(bytes,"UTF-8");
        System.out.println("time server receive order:"+clientMsg);

        // 向客户端发送消息
        String serverReply= "hello client!";
        bytes = serverReply.getBytes();
        // 获取 buffer
        ByteBuf buf = Unpooled.buffer(bytes.length);
        buf.writeBytes(bytes);
        ctx.writeAndFlush(buf);


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 这里传入的 ChannelHandlerContext 可以理解为 SocketChannel 的代理
        ctx.close();
    }
}
