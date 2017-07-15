package com.LineBaseFrameDecoder.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


/**
 * Created by zhujiating on 2017/7/3.
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
    /**
     * 这里的泛型需要注意，引入的 netty 中的SocketChannel
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new TimeServerHandler());
    }
}
