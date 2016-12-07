package com.liaozl.netty.nio;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liaozuliang
 * @date 2016-12-07
 */
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;

    private static final int BUFFER_SIZE = 1024;

    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open(); // 打开、创建多路复用器

            serverSocketChannel = ServerSocketChannel.open(); // 打开、创建通道
            serverSocketChannel.configureBlocking(false); // 设置非阻塞
            serverSocketChannel.bind(new InetSocketAddress(port), BUFFER_SIZE); // 绑定端口, 设置backlog大小
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 监听连接

            System.out.println("The server is start in port: " + port);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }


    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
                    iterator.remove();

                    try {
                        handleInput(selectionKey);
                    } catch (Exception e) {
                        e.printStackTrace();

                        if (selectionKey != null) {
                            selectionKey.cancel();
                            if (selectionKey.channel() != null) {
                                selectionKey.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (!selectionKey.isValid()) {
            return;
        }

        if (selectionKey.isAcceptable()) { // 连接客户端
            ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel(); // ssc = serverSocketChannel
            SocketChannel socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ); // 监听数据读取
        }

        if (selectionKey.isReadable()) { // 读取数据
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

            ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            int readBytes = socketChannel.read(readBuffer);
            if (readBytes > 0) { // 读取数据
                readBuffer.flip(); // 重置标记位置
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);

                String body = new String(bytes, "UTF-8");
                System.out.println("The server receive order: " + body);

                String currentTime = "QUERY TIME ORDER".equals(body) ? (new Date().toLocaleString()) : "BAD ORDER";
                doWrite(socketChannel, currentTime);
            } else if (readBytes < 0) { // 对端链路关闭
                selectionKey.cancel();
                socketChannel.close();
            } else { // 读到0字节，忽略

            }
        }
    }

    private void doWrite(SocketChannel socketChannel, String data) throws IOException {
        if (StringUtils.isNotBlank(data)) {
            byte[] bytes = data.getBytes();

            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);

            writeBuffer.flip(); // 重置标记位置
            socketChannel.write(writeBuffer);
        }
    }
}
