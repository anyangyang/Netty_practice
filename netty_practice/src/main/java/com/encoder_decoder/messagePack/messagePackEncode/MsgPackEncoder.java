package com.encoder_decoder.messagePack.messagePackEncode;

        import io.netty.buffer.ByteBuf;
        import io.netty.channel.ChannelHandlerContext;
        import io.netty.handler.codec.MessageToByteEncoder;
        import org.msgpack.MessagePack;

/**
 * Created by zhujiating on 2017/7/12.
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, ByteBuf byteBuf) throws Exception {
        // 首先创建 MessagePack 对象，利用 MessagePack 的 write(),方法对传入的 Object 对象进行序列化（转化成字节数组），最后将 字节数组中的数据添加到 ByteBuf 中
        MessagePack msgPack = new MessagePack();
        // 开始序列化
        byte[] bytes = msgPack.write(object);
        // 将字节数组 写入到 ByteBuf 中
        byteBuf.writeBytes(bytes);
    }
}
