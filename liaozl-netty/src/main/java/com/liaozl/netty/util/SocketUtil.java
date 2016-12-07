package com.liaozl.netty.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author liaozuliang
 * @date 2016-12-06
 */
public class SocketUtil {

    public static void close(Socket socket, BufferedReader in, PrintWriter out) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            out = null;
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }

    public static void closeServer(ServerSocket serverSocket) throws IOException {
        if (serverSocket != null) {
            System.out.println("The server close");
            serverSocket.close();
            serverSocket = null;
        }
    }
}
