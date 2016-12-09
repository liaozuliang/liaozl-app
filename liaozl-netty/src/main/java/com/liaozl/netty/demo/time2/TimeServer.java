package com.liaozl.netty.demo.time2;

import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
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
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Date;


/**
 * 换行分割符，不支持TCP粘包
 *
 * @author liaozuliang
 * @date 2016-12-08
 */
public class TimeServer {

    public static void main(String[] args) throws Exception {
        new TimeServer().bind(NettyConstant.PORT);
    }

    private void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, NettyConstant.BUFFER_SIZE);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new TimeServerHandler());
                }
            });

            // 绑定端口，同步等待成功
            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("The time server is start in port: " + port);

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class TimeServerHandler extends ChannelHandlerAdapter {

        private int counter;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);

            String body = new String(bytes, "UTF-8").substring(0, bytes.length - NettyConstant.LINE_SEPARATOR.length());
            System.out.println("The time server receive order: " + body + "; the couter is: " + (++counter));

            String currentTime = "QUERY TIME ORDER".equals(body) ? new Date().toLocaleString() : "BAD ORDER";
            currentTime += NettyConstant.LINE_SEPARATOR;
            ByteBuf writeBuf = Unpooled.copiedBuffer(currentTime.getBytes());
            ctx.write(writeBuf);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}