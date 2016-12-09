package com.liaozl.netty.demo.time3;

import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 支持TCP粘包
 *
 * @author liaozuliang
 * @date 2016-12-08
 */
public class TimeClient {

    public static void main(String[] args) throws Exception {
        new TimeClient().connect("localhost", NettyConstant.PORT);
    }

    private void connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    // 新增解码器，解决TCP粘包问题
                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(NettyConstant.BUFFER_SIZE));
                    socketChannel.pipeline().addLast(new StringDecoder());

                    socketChannel.pipeline().addLast(new TimeClientHandler());
                }
            });

            ChannelFuture f = bootstrap.connect(host, port).sync();
            System.out.println("connected to server");

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private class TimeClientHandler extends ChannelHandlerAdapter {

        private int counter;
        private byte[] msgBytes;

        public TimeClientHandler() {
            msgBytes = ("QUERY TIME ORDER" + NettyConstant.LINE_SEPARATOR).getBytes();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ByteBuf writeBuf = null;
            for (int i = 0; i < 100; i++) {
                writeBuf = Unpooled.buffer(msgBytes.length);
                writeBuf.writeBytes(msgBytes);
                ctx.writeAndFlush(writeBuf);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String body = (String) msg;
            System.out.println("Now is: " + body + "; the counter is: " + (++counter));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}
