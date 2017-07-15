package com.lima.nio.NIO2.server;

/**
 * Created by zhujiating on 2017/7/1.
 */
public class TimeServer {
    public static void main(String[] arg){
        AsynchronousServerHandler asynchronousServerHandler = new AsynchronousServerHandler(8080);
        new Thread(asynchronousServerHandler).start();
    }
}
