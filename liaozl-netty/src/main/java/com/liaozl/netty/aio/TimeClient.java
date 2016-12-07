package com.liaozl.netty.aio;

/**
 * 客户端，AIO，异步非阻塞模式
 *
 * @author liaozuliang
 * @date 2016-12-07
 */
public class TimeClient {

    private static final String serverIp = "localhost";
    private static final int serverPort = 8080;

    private static void start() {
        new Thread(new AsyncTimeClientHandler(serverIp, serverPort), "AIO-AsyncTimeClientHandler-001").start();
    }

    public static void main(String[] args) {
        start();
    }
}
