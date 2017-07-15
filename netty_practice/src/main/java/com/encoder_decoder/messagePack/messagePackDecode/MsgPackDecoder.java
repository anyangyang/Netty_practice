package com.encoder_decoder.messagePack.messagePackDecode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by zhujiating on 2017/7/15.
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        /**
         * 1、从 ByteBuf 对象中获取需要解码的 byte 数组
         * 2、调用 MessagePack 的 read 方法将 byte 数组反序列化成一个 Object 对象
         * 3、最后将解码的对象加入到 List 对象中
         */

        // 1、从 ByteBuf 对象中获取需要解码的 byte 数组
        byte[] bytes = new byte[byteBuf.readableBytes()];
        // 2、调用 MessagePack 的 read 方法将 byte 数组反序列化成一个 Object 对象,   byteBuf 的 readerIndex 是读 ByteBuf 的起始位置，从这个位置开始读取数据
        byteBuf.getBytes(byteBuf.readerIndex(),bytes,0,bytes.length);
        // 3、最后将解码的对象加入到 List 对象中
        MessagePack msgPack = new MessagePack();
        list.add(msgPack.read(bytes));

    }
}
