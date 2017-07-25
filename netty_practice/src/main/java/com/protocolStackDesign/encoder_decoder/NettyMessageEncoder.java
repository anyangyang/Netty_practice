package com.protocolStackDesign.encoder_decoder;

import com.encoder_decoder.MarShallingTest.MarShallingCodeFactory;
import com.protocolStackDesign.MarshallingCodeFactory;
import com.protocolStackDesign.nettyMessage.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

/**
 * Created by zhujiating on 2017/7/25.
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage>{

    // 持有 NettyMarshallingEncoder (NettyShallingEncoder 继承了 MarshallingEncoder)
    NettyMarshallingEncoder marshallingEncoder;


    NettyMessageEncoder(){
        this.marshallingEncoder = MarshallingCodeFactory.buildMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {

        //  首先判断一下 msg 是否为空
        if(msg == null || msg.getNettyHeader() == null)
            throw new Exception("the encoder message is null");

        ByteBuf byteBuf = Unpooled.buffer();
        // netty 私有协议标识 + 主次版本号,int
        byteBuf.writeInt(msg.getNettyHeader().getCrcCode());
        // netty 消息长度，int
        byteBuf.writeInt(msg.getNettyHeader().getMsgLength());
         // 会话 id
        byteBuf.writeLong(msg.getNettyHeader().getSessionId());
        // 消息类型
        byteBuf.writeByte(msg.getNettyHeader().getMsgType());
        // 消息的优先级
        byteBuf.writeByte(msg.getNettyHeader().getPriority());
        // 有几条扩展消息
        byteBuf.writeInt(msg.getNettyHeader().getAttachment().size());

        /**
         * 接下来处理扩展消息
         * 1、key 用来存储从 Map 中存放的 key
         * 2、keyArray 用来存储 Key 转换成个 字节流
         * 3、value 用于存放 Map 中对应的某个key的值
         */

        String key;
        byte[] keyArray;
        Object value;

        for(Map.Entry<String,Object> param : msg.getNettyHeader().getAttachment().entrySet()){
            key = param.getKey();
            keyArray = key.getBytes("utf-8");
            // 现在 ByteBuf 中压入 keyArray 的长度
            byteBuf.writeInt(keyArray.length);
            // 再写入 keyArray
            byteBuf.writeBytes(keyArray);
            // 然后再处理 键值对中的值
             value = param.getValue();
             // 有 NettyMarshallingEncoder 来处理
            marshallingEncoder.encode(ctx,value,byteBuf);
        }

        // 将 key value keyArray 置为空，以便垃圾回收
        key = null;
        keyArray = null;
        value = null;

        // 最后处理一下 MessageBody
        if(msg.getNettyBody() != null){
            marshallingEncoder.encode(ctx,msg.getNettyBody(),byteBuf);
        }

        // 下面的代码会与 《netty 权威指南》 中的不同。
        int readableBytes = byteBuf.readableBytes();

        byteBuf.setInt(4,readableBytes);

        out.add(byteBuf);

    }
}
