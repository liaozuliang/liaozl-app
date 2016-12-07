package com.liaozl.netty.nio;

/**
 * 服务端，NIO，非阻塞模式
 *
 * @author liaozuliang
 * @date 2016-12-07
 */
public class TimeServer {

    private static final int port = 8080;

    private static void start() {
        MultiplexerTimeServer server = new MultiplexerTimeServer(port);
        new Thread(server, "NIO-TimeServer-001").start();
    }

    public static void main(String[] args) {
        start();
    }
}
