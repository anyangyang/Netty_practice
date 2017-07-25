package com.protocolStackDesign;

import com.protocolStackDesign.encoder_decoder.NettyMarshallingDecoder;
import com.protocolStackDesign.encoder_decoder.NettyMarshallingEncoder;
import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * Created by zhujiating on 2017/7/25.
 */
public class MarshallingCodeFactory  {

    public static NettyMarshallingDecoder buildMarshallingDecoder(){
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory,configuration);
        // NettyMarshallingDecoder 继承了 MarshallingDecoder
        NettyMarshallingDecoder decoder = new NettyMarshallingDecoder(provider,10240);
        return decoder;
    }

    public static NettyMarshallingEncoder buildMarshallingEncoder(){
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory,configuration);
        // NettyMarshallingEncoder 继承了 MarshallingEncoder
        NettyMarshallingEncoder encoder = new NettyMarshallingEncoder(provider);
        return encoder;
    }
}
