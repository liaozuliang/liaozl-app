package com.liaozl.netty.aio;

import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * @author liaozuliang
 * @date 2016-12-07
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel) {
        if (this.socketChannel == null) {
            this.socketChannel = socketChannel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer readBuffer) {
        readBuffer.flip();

        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);

        try {
            String response = new String(bytes, "UTF-8");
            System.out.println("The time server receive order: " + response);

            String currentTime = "QUERY TIME ORDER".equals(response) ? new Date().toLocaleString() : "BAD ORDER";
            doWrite(currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String data) {
        if (StringUtils.isNotBlank(data)) {
            byte[] bytes = data.getBytes();

            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);

            writeBuffer.flip();
            socketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    if (buffer.hasRemaining()) {
                        socketChannel.write(buffer, buffer, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer buffer) {
                    try {
                        socketChannel.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer readBuffer) {
        try {
            this.socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
