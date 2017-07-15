package com.lima.nio.NIO1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhujiating on 2017/6/30.
 */
public class ClientServer {

        int port;
        String host="127.0.0.1";
        public ClientServer(int port){
            this.port = port;
        }

   // 这个类中最核心的部分
    public void start(){
        try{
            // 先打开一个 选择器和SocketChannel
            Selector selector = Selector.open();
            SocketChannel sc = SocketChannel.open();
            // 设置为不阻塞
            sc.configureBlocking(false);
            // 判断有没有连接成功
            if(sc.connect(new InetSocketAddress(host,port))){
                // 如果连接成功，那么就在选择器中注册 OP_READ
                sc.register(selector,SelectionKey.OP_READ);
                // 向服务端发送消息
                doWrite(sc);
            }else{
                // 连接没有成功，就注册 OP_Connct
                sc.register(selector, SelectionKey.OP_CONNECT);
            }

            while(true){
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();

                while(it.hasNext()){
                    SelectionKey key = it.next();
                    // 处理拿到的key
                    processKey(key,selector);
                    it.remove();

                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }


    }

    public void processKey(SelectionKey key,Selector selector)throws IOException{
        // 先判断 key 是否合法
        if(key.isValid()){
            // 先取出一个通道
            SocketChannel sc = (SocketChannel)key.channel();
            // 连接操作
            if(key.isConnectable()){
                // 判断有没有连接成功
                try{
                    // 如果连接成功，那就注册 selectionKey.OP_READ
                    if(sc.finishConnect()){
                        sc.register(selector,SelectionKey.OP_READ);
                        doWrite(sc);
                    }else{
                        // 连接失败，进程退出
                        System.exit(1);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }


            }

            if(key.isReadable()){
                // 创建一个 Bytebuffer
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                // 将服务端读取的数据从通道中取到buffer
                int capacity =  sc.read(buffer);
                // 如果缓存中有数据
                if(capacity > 0){
                    buffer.flip();
                    //创建一个 byte[],将数据读取到 byte[] 中
                    byte[] bytes = new byte[buffer.remaining()];

                    buffer.get(bytes);
                    // 将字节数组转换成字符串
                    String serverReply = new String(bytes,"UTF-8");
                    System.out.println("client server receive response:"+serverReply);
                }


            }


        }

            // 将 key 在选择器中的注册撤销掉
             //key.cancel();
    }



    public void doWrite(SocketChannel sc){
        String order = "hello server";
        // 转换成字符串
        byte[] bytes = order.getBytes();
        // 申请一个 ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        // 将数据 byte 的数据读取到 缓存中
        buffer.put(bytes);
        // 将 缓存的 limit 设置成 当前的 position，再将当前的 position 设置为0
        buffer.flip();
        // 调用 sc 发送数据
        try{
            sc.write(buffer);
        }catch(IOException e){
            e.printStackTrace();
        }

        if(!buffer.hasRemaining()){
            System.out.println("success!");
        }

    }


   public static void main(String[] args){
        ClientServer cs = new ClientServer(8080);
        cs.start();
   }
}
