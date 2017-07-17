package com.encoder_decoder.messagePack.server;

import com.encoder_decoder.serializable.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by zhujiating on 2017/7/15.
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    private int counter = 0;

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
         List<UserInfo> message = (List<UserInfo>)msg;
         System.out.println("server receive client the MsgPack message:"+msg);

         // 向客户端发送消息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserID(counter++);
        userInfo.setUserName("hello client");
        ctx.writeAndFlush(userInfo);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
         ctx.flush();
    }
}
