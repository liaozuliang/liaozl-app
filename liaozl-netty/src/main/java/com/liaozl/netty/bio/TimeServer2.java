package com.liaozl.netty.bio;

import com.liaozl.netty.executor.TimeServerHandlerExecutePool;
import com.liaozl.netty.util.SocketUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端，伪异步式I/O(通过线程池控制socket连接数量)
 *
 * @author liaozuliang
 * @date 2016-12-06
 */
public class TimeServer2 {

    private static int port = 8080;
    private static ServerSocket serverSocket = null;
    private static TimeServerHandlerExecutePool pool = new TimeServerHandlerExecutePool(10, 100);

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
                pool.execute(new TimeServerHandler(serverSocket, socket, pool));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SocketUtil.closeServer(serverSocket);
        }
    }


}
