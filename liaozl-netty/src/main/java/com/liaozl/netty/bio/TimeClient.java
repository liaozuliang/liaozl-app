package com.liaozl.netty.bio;

import com.liaozl.netty.util.SocketUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 客户端，同步阻塞式I/O
 *
 * @author liaozuliang
 * @date 2016-12-06
 */
public class TimeClient {

    private static String SERVER_IP = "localhost";
    private static int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException {
        start();
    }

    private static void start() throws IOException {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("QUERY TIME ORDER");
            System.out.println("Send order to server succeed.");

            String response = in.readLine();
            System.out.println("Now is: " + response);

            out.println("CLOSE SERVER");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SocketUtil.close(socket, in, out);
        }
    }

}
