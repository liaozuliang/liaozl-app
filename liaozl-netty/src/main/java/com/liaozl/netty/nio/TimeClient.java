package com.liaozl.netty.nio;

/**
 * 客户端，NIO，非阻塞模式
 * @author liaozuliang
 * @date 2016-12-07
 */
public class TimeClient {

    private static final String serverIp = "localhost";
    private static final int serverPort = 8080;

    private static void start(){
        new Thread(new TimeClientHandler(serverIp, serverPort)).start();
    }

    public static void main(String[] args) {
        start();
    }
}
