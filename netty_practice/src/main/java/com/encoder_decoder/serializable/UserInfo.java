package com.encoder_decoder.serializable;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by zhujiating on 2017/7/10.
 */
public class UserInfo implements Serializable{
    private final long serialVersionUid = 1L;
    private String userName;
    private int userID;

    // 生成 getter and setter 方法


    public long getSerialVersionUid() {
        return serialVersionUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    // 这里是一个 利用 ByteBuffer 的方式来进行编解码
    public byte[] codeUserInfo(){
        // 先申请一个 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将 userName 字符串转换成字节数组
        byte[] bytes = userName.getBytes();
        // 将 userName 和 userID 填充到 ByteBuffer 中
        byteBuffer.putInt(bytes.length);          // userName 转换成字节数组之后的长度
        byteBuffer.put(bytes);                    // userName 字节数组
        byteBuffer.putInt(userID);
        byteBuffer.flip();               // 将 ByteBuffer 的 limit 置为当前的 position，将 position 置为 0;
        bytes = null;
        // 在将 byteBuffer 转换成字节数组
        byte[] serialBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(serialBytes);

        return serialBytes;

    }
}
