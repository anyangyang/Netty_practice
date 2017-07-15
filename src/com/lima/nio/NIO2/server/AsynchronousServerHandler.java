package com.lima.nio.NIO2.server;

import com.lima.nio.NIO2.server.AcceptHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * 这里写的类完全参考了《netty权威指南》第二章 AIO
 * Created by zhujiating on 2017/7/1.
 */


public class AsynchronousServerHandler implements  Runnable{

    // 服务器端需要监听的端口号
    int port;
    // 同步操作的辅助类,他的作用是，在完成一组正在执行的操作之前，允许当前线程一直阻塞
    CountDownLatch latch;
    // 服务器端的异步socket通道
    AsynchronousServerSocketChannel assc ;

    public AsynchronousServerHandler(int port) {
        // 传入端口
        this.port = port;

        try{
            // 创建一个 服务器端的 异步socket通道
            assc = AsynchronousServerSocketChannel.open();
            // 绑定需要监听的端口
            assc.bind(new InetSocketAddress(port));
            // 输出一下，服务器正在监听
            System.out.println("time server is start in port:"+port);
        }catch(IOException e){
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        // 先创建一个同步辅助类 ,传入参数是一个 count（如下：count = 1）
        latch = new CountDownLatch(1);
        // 监听 accept 是否成功,AcceptHandler类的作用是用来接收 accept 事件是否成功
        assc.accept(this,new AcceptHandler());

        try {
            latch.await();   // await()的作用是使 当前线程在锁存器倒计数至零之前一直等待，除非线程被中断。
                             // 锁存器的计数？
        }catch(InterruptedException e){
            e.printStackTrace();
        }

    }


}
