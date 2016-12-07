package com.liaozl.netty.bio;

import com.liaozl.netty.executor.TimeServerHandlerExecutePool;
import com.liaozl.netty.util.SocketUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author liaozuliang
 * @date 2016-12-06
 */
public class TimeServerHandler implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private TimeServerHandlerExecutePool pool;

    public TimeServerHandler(ServerSocket serverSocket, Socket socket) {
        this.serverSocket = serverSocket;
        this.socket = socket;
    }

    public TimeServerHandler(ServerSocket serverSocket, Socket socket, TimeServerHandlerExecutePool pool) {
        this.serverSocket = serverSocket;
        this.socket = socket;
        this.pool = pool;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);

            String currentTime = null;
            String body = null;

            while (true) {
                body = in.readLine();
                if (body == null) {
                    System.out.println("close connection");
                    break;
                }

                if ("CLOSE SERVER".equals(body)) {
                    if (pool != null) {
                        TimeServerHandlerExecutePool.close(pool);
                    }

                    SocketUtil.closeServer(serverSocket);

                    break;
                }

                System.out.println("The time server receive order: " + body);

                currentTime = "QUERY TIME ORDER".equals(body) ? (new Date().toLocaleString()) : "BAD ORDER";
                out.println(currentTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SocketUtil.close(socket, in, out);
        }
    }
}
