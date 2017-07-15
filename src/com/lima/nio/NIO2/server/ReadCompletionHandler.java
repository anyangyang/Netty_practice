package com.lima.nio.NIO2.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by zhujiating on 2017/7/1.
 */
public class ReadCompletionHandler implements CompletionHandler<Integer,ByteBuffer> {
    private AsynchronousSocketChannel socketChannel;

    // 构造函数
    ReadCompletionHandler(AsynchronousSocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
            // 执行这个函数的时候，客户端发送的已经加载到了 ByteBuffer 中
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            try{
                String clientOrder = new String(bytes,"UTF-8");
                System.out.println("server receive order:"+clientOrder);

                //给客户端回信
                doWrite();

            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

    }

    public void doWrite(){
        String serverReply = "hello client";
        byte[] bytes = serverReply.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        // 将要发送的数据传入到缓存中
        writeBuffer.put(bytes);
        writeBuffer.flip();
        // 下面用了一个匿名类的方式来实现
        socketChannel.write(writeBuffer,writeBuffer,new CompletionHandler<Integer,ByteBuffer>(){
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                // 如果没有发送完成，就继续发送
                if(buffer.hasRemaining()){
                    socketChannel.write(buffer,buffer,this);
                }else{
                    System.out.println("success!");
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                // 如果发送失败，关闭通道
                try{
                    socketChannel.close();
                }catch(IOException e){
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try{
            socketChannel.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
