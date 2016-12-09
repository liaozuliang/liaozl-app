package com.liaozl.netty.demo.echo;

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
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 自定义分隔符解码器，解决TCP粘包、拆包、读半包问题
 *
 * @author liaozuliang
 * @date 2016-12-09
 */
public class EchoServer {

    public static void main(String[] args) {
        new EchoServer().bind(NettyConstant.PORT);
    }

    private void bind(int port) {
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
                    // 自定义分隔符解码器
                    ByteBuf delimiter = Unpooled.copiedBuffer(NettyConstant.MSG_SEPARATOR.getBytes());
                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(NettyConstant.BUFFER_SIZE, delimiter));
                    socketChannel.pipeline().addLast(new StringDecoder());

                    socketChannel.pipeline().addLast(new EchoServerChannelHandler());
                }
            });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("The EchoServer is start in port: " + port);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class EchoServerChannelHandler extends ChannelHandlerAdapter {

        private int counter;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String body = (String) msg;
            System.out.println("The EchoServer receive a msg: " + body + "; the counter is: " + (++counter));

            body += NettyConstant.MSG_SEPARATOR;
            ByteBuf writeBuf = Unpooled.copiedBuffer(body.getBytes());

            ctx.writeAndFlush(writeBuf);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }

    }
}
