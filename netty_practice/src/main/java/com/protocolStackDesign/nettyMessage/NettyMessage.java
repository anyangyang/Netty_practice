package com.protocolStackDesign.nettyMessage;

/**
 * Created by zhujiating on 2017/7/25.
 */
public class NettyMessage{

    /**
     * 消息头
     */
   private NettyHeader nettyHeader;
    /**
     * 消息体
     */
   private Object nettyBody;

    public NettyHeader getNettyHeader() {
        return nettyHeader;
    }

    public void setNettyHeader(NettyHeader nettyHeader) {
        this.nettyHeader = nettyHeader;
    }

    public Object getNettyBody() {
        return nettyBody;
    }

    public void setNettyBody(Object nettyBody) {
        this.nettyBody = nettyBody;
    }


    //todo
    // 重写 toString()
}
