package com.liaozl.netty.demo.time;

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

/**
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

        private final ByteBuf firstMsg;

        public TimeClientHandler() {
            byte[] bytes = "QUERY TIME ORDER".getBytes();
            firstMsg = Unpooled.buffer(bytes.length);
            firstMsg.writeBytes(bytes);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(firstMsg);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf msgBuf = (ByteBuf) msg;
            byte[] bytes = new byte[msgBuf.readableBytes()];
            msgBuf.readBytes(bytes);

            String body = new String(bytes, NettyConstant.UTF8);
            System.out.println("Now is: " + body);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}
