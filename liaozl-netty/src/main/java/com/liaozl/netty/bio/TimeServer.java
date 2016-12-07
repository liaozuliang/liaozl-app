package com.liaozl.netty.bio;

import com.liaozl.netty.util.SocketUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端，同步阻塞式I/O
 *
 * @author liaozuliang
 * @date 2016-12-06
 */
public class TimeServer {

    private static int port = 8080;
    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws IOException {
        start();
    }

    private static void start() throws IOException {
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("The time server is start in port: " + port);
            while (true) {
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(serverSocket, socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SocketUtil.closeServer(serverSocket);
        }
    }

}
