package com.encoder_decoder.MarShallingTest.client;

import com.encoder_decoder.serializable.UserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zhujiating on 2017/7/19.
 */
public class MarshallingClientHandler extends ChannelHandlerAdapter {
    private int sendNumber;
    MarshallingClientHandler(int sendNumber){
        this.sendNumber = sendNumber;
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接成功,开始向服务端发送数据
        UserInfo[] userInfos = userInfos();
        for(int i=0;i<sendNumber;i++){
            // 每次循环发送一个对象，这里面会经过自己创建的编码器进行编码的过程
            ctx.writeAndFlush(userInfos[i]);
        }

    }

    private UserInfo[] userInfos(){
        UserInfo[] userInfos = new UserInfo[sendNumber];
        for(int i=0;i<sendNumber;i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setUserID(i);
            userInfo.setUserName("hello server---->"+i);
            userInfos[i]=userInfo;
        }
        return userInfos;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client receive server response:"+msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
