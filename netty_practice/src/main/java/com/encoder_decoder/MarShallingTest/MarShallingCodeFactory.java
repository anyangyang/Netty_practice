package com.encoder_decoder.MarShallingTest;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * Created by zhujiating on 2017/7/17.
 */
public final class MarShallingCodeFactory {

    /**
     * 创建 jboss MarShalling 解码器
     */
    public static MarshallingDecoder buildMarshallingDecoder(){
        // 获取 MarshallerFactory 实例，传入的参数 serial 表示获取的是 java 序列化工厂对象
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        // 获取 MarshallingConfiguration 实例，并设置版本号为：5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        // 创建 provider
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory,configuration);
        // 创建 Decoder,传入参数是 UnmarshallerProvider，以及单个消息序列化后的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider,1024);
        return decoder;
    }

    public static MarshallingEncoder buildMarshallingEncoder(){
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        // 下面这条语句与创建 decoder 会有点不同
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory,configuration);
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }

}
