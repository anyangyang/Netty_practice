package com.encoder_decoder.serializable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by zhujiating on 2017/7/10.
 */
public class TestSerialiable {

    public static void main(String[] args){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserID(100);
        userInfo.setUserName("welcome to Netty");

        // 开始序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(userInfo);  // 将序列化流 写入传入的 ByteArrayOutputStream
            os.flush();      // 写入所有已经缓冲的字节，并刷新到底层流中
            os.close();
            byte[] bytes = bos.toByteArray(); // 底层流  ByteArrayOutputStream 将序列化对象写入的数据 转换成 字节数组

            System.out.println("the jdk serializable length is:"+bytes.length);
            bos.close();
            System.out.println("the byte array serializable length is:"+userInfo.codeUserInfo().length);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
