package com.protocolStackDesign.nettyMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhujiating on 2017/7/25.
 * 自定义的协议
 */
public class NettyHeader {

    /**
     * 0xabef 是 netty 消息的标识
     * 第一个 0x01 是 netty 的主版本号
     * 第二个 0x01 是 netty 的次版本号
     */
    private final int crcCode = 0xabef0101;
    /**
     * 消息的长度
     */
    private int msgLength;
    /**
     * 会话ID
     */
    private long sessionId;
    /**
     * 消息的类型：用字节表示
     * 消息的类型根据客户端和服务端的职位不同，类型不同
     * 客户端：
     *          1、握手请求消息
     *          2、业务请求消息
     *          3、心跳请求消息
     * 服务端：
     *          4、握手响应消息
     *          5、业务响应消息
     *          6、心跳响应消息
     */
    private byte msgType;
    /**
     * 消息的优先级
     */
    private byte priority;
    /**
     * 消息头部的扩展，用于存放一些在消息头部没有定义的键值对
     */
    private Map<String,Object> attachment = new HashMap<String, Object>();


    public int getCrcCode() {
        return crcCode;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    // todo
    // 重写 toString()
}
