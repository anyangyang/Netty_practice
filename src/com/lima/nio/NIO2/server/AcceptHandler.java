package com.lima.nio.NIO2.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by zhujiating on 2017/7/1.
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel,AsynchronousServerHandler> {



    @Override
    public void completed(AsynchronousSocketChannel result, AsynchronousServerHandler attachment) {         // 返回一个 异步 SocketChannel
        // 继续监听
       attachment.assc.accept(attachment,this);
       ByteBuffer buffer = ByteBuffer.allocate(1024);


        /**
         * 分别说一下面三个参数：
         * 第一个buffer 接收缓冲区，用于从异步 channel 中读取数据包
         * 第二个buffer 异步channel 携带的附件，通知回调的时候，作为入参使用
         * 第三个ReadCompletionHandler的实例：接收通知回调业务的 handler
         */
        result.read(buffer,buffer,new ReadCompletionHandler(result));

    }

    @Override
    public void failed(Throwable exc, AsynchronousServerHandler attachment) {
            exc.printStackTrace();
            attachment.latch.countDown(); // 锁存器减一
    }
}
