package com.liaozl.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liaozuliang
 * @date 2016-12-07
 */
public class TimeClientHandler implements Runnable {

    private String serverIp;
    private int serverPort;

    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop;
    private static final int BUFFER_SIZE = 1024;

    public TimeClientHandler(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

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

        SocketChannel sc = (SocketChannel) selectionKey.channel();
        if (selectionKey.isConnectable()) {
            if (sc.finishConnect()) {
                sc.register(selector, SelectionKey.OP_READ);
                doWrite(sc);
            } else {
                System.exit(1);
            }
        }

        if (selectionKey.isReadable()) {
            ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            int readBytes = sc.read(readBuffer);
            if (readBytes > 0) {
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);

                String body = new String(bytes, "UTF-8");
                System.out.println("Now is: " + body);

                this.stop = true;
            } else if (readBytes < 0) {
                selectionKey.cancel();
                sc.close();
            } else {

            }
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] bytes = "QUERY TIME ORDER".getBytes();

        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);

        writeBuffer.flip();
        sc.write(writeBuffer);

        if (!writeBuffer.hasRemaining()) {
            System.out.println("Send order to server succeed.");
        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(serverIp, serverPort))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }
}
