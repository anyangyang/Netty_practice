package com.lima.nio.NIO1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * 用来练习nio，利用 nio 创建一个简单的服务器
 * Created by zhujiating on 2017/6/30.
 */
public class TimeServer {

    // 定义一个端口号
    int port=8080;

//    public TimeServer(int port){
//        this.port = port ;
//    }

    public void start(){
        try{
            // 首先打开 ServerSocketChannel,用于监听客户端的连接
            ServerSocketChannel ssc = ServerSocketChannel.open();
            // 绑定 InetSocketAddress（套接字地址），ServerSocketAddress 要监听的地址
            ssc.socket().bind(new InetSocketAddress(port));
            //设置连接模式为非阻塞模式
            ssc.configureBlocking(false);
            //创建一个多路复用选择器（选择器的作用暂时先不用去管，等会再来补全
            Selector selector = Selector.open();
            // 创建一个 ReactorTask 线程，并且启动这个线程
            //  new Thread(new ReactorTask()).start();     //这一句代码会报错，暂时先跳过
            // 将 ServerSccketChannel 注册到 选择器中
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            // 在控制台输出一下
            System.out.println("server is start in port:"+port);

            /* 接下来 调用选择器的 select() ,selector 会轮循 他所管理的 ServerSocketChannel 如果当前 ServerSocketChannel 活跃，
               就将当前 ServerSocketChannel 的SelectionKey放在以选择集中，具体的情况可以在 JDK 文档中查看
            */
            Set<SelectionKey> selectionkeys = null;
            while(true){
                // 先建立一个 集合 用于存放 selector 返回的选择集（用 SelectionKey 标识）,返回的操作集表示哪些 ServerSocketChannel 是活跃的

                try{
                    // 选择器将活跃的 ServerSocketChannel 的 SelecionKey 集合 加到选择集中
                    selector.select();
                }catch(Exception e){
                    e.printStackTrace();
                    // 如果发生异常，就退出
                    break;
                }
                //  从 选择器中取出选择集
                selectionkeys = selector.selectedKeys();
                    /*
                    获取 selectionKey Set 之后，遍历 set 从中取出 seletionKey，根据不同的 selectionKey 做不同的操作
                     */

                // 首先获取  Set 的迭代器
                Iterator<SelectionKey> it = selectionkeys.iterator();
                // 开始遍历 Set
                while(it.hasNext()){
                    // 先取出一个 key，key中保存着对应的 就绪操作集
                    SelectionKey key = it.next();
                    // 对 key 进行相应的处理
                    processKey(key,selector);
                    // 处理完成之后，将 key 从集合中删除
                    it.remove();
                    // 走到这里就表示当前key所代表的通道的就绪操作集已经处理完成了,就删除该 key（一个key代表着一个通道在selector中的注册），也就是 当前 Key 所代表的通道在 selector 在通道中的注册
//                        if(key != null)
//                        key.cancel();
//
//                        // 取消注册之后，再关闭通道
//                        if(key.channel() != null)
//                            key.channel().close();


                }



            }

            // 跳出循环的情况，表示上面可能发生了异常，那么就 关闭selector，关闭 selector 之后，上面的注册的通道（channel）和管道（pipe）都是自动注册并关闭
            if(selector != null){
                selector.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }


    }




    // 对传入的 key 所代表的通道都是活跃的，所以，就要根据不同的key中的就绪操作集，做不同的处理
    private void processKey(SelectionKey key,Selector selector)throws IOException{
        // 先对传入的 key 进行判断，是否合法（合法的定义：当前 key 所代表的 channel 是否还注册在 selector 中
        if(key.isValid()){
            // 如果是合法的，在判断 key 当前的操作集是否是 acceptable 有客户端的连接请求
            if(key.isAcceptable()){
                // 先取得 服务器的通道
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();

                // 建立了连接，返回一个 SocketChannel 来进行对话
                SocketChannel sc = ssc.accept();
                // 将SocketChannel 设置为不阻塞
                sc.configureBlocking(false);
                //  将 SocketChannel 注册到 选择器中,选择器已经通过参数形式传入到了方法中
                sc.register(selector,SelectionKey.OP_READ);


            }
            // 判断key当前的操作集是否为读操作（这里的读操作是指服务器中有数据发送过来
            if(key.isReadable()){
                // 先从 key 从取出通道，因为 服务器端和客户端主要通过 通道来进行通信，而且这里的通道是全双工的，与 BIO 的方式不同
                SocketChannel sc = (SocketChannel)key.channel();
                // 申请 ByteBuffer，暂时先申请 1024 个字节大小的缓存
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 将客户端传送进来的数据读到缓存中，接下来的操作，我们假设传过来的数据都在1024个字节之内，这样的话，就不用循环读取

                // 通道将数据读取到缓存中，返回读取的字节数
                int byteCount = sc.read(buffer);
                if(byteCount > 0){
                    // 缓存中有内容，那么就将缓存的成员变量 limit 设置为 当前的 position ，再将 position 设置为0
                    buffer.flip();
                    // 创建一个 Byte 数组，用于存放从缓冲区中读出的内容,Byte 数组的大小为缓存的 limit - position (缓存的 remaining（）提供了这样一个值）
                    byte[] bytes = new byte[buffer.remaining()];
                    // 将缓冲区的值读取到 字节数组中
                    buffer.get(bytes);
                    // 将字节数组中的值转换成字符串，使用 utf-8 编码
                    String clientOrder = new String(bytes,"UTF-8");
                    System.out.println("Server recieve odeer : " + clientOrder);

                    //向客户端发送响应
                    doWrite(sc);
                }




            }

            // 上面的处理完成之后，删除当前 key 在选择器中的注册
           // key.cancel();
        }
    }

    public void doWrite(SocketChannel sc){
        String order = "hello client";
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

    // main 方法用于测试
    public static void main(String[] args){
        TimeServer server = new TimeServer();
        server.start();
    }
}
