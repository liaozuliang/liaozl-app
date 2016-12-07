package com.liaozl.netty.aio;

import com.liaozl.netty.nio.MultiplexerTimeServer;

/**
 * 服务端，AIO，异步非阻塞模式
 *
 * @author liaozuliang
 * @date 2016-12-07
 */
public class TimeServer {

    private static final int port = 8080;

    private static void start() {
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(asyncTimeServerHandler, "AIO-AsyncTimeServer-001").start();
    }

    public static void main(String[] args) {
        start();
    }
}
