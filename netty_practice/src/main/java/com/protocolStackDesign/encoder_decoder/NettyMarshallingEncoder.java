package com.protocolStackDesign.encoder_decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

/**
 * Created by zhujiating on 2017/7/25.
 */
public class NettyMarshallingEncoder extends MarshallingEncoder{
    // 重写构造方法
    public NettyMarshallingEncoder(MarshallerProvider provider) {
        super(provider);
    }

    // 重写 encoder()


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
