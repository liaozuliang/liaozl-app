package com.liaozl.netty.demo.serialization.java;

import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Java序列化
 *
 * @author liaozuliang
 * @date 2016-12-09
 */
public class SubReqClient {

    public static void main(String[] args) {
        new SubReqClient().connect(NettyConstant.IP, NettyConstant.PORT);
    }

    private void connect(String host, int port) {
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ObjectDecoder(
                            NettyConstant.BUFFER_SIZE,
                            ClassResolvers.cacheDisabled(this.getClass().getClassLoader())
                    ));
                    socketChannel.pipeline().addLast(new ObjectEncoder());
                    socketChannel.pipeline().addLast(new SubReqClientChannelHandler());
                }
            });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("The SubReqClient connect to server succeed");

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    private class SubReqClientChannelHandler extends ChannelHandlerAdapter {

        private SubscribeRequest getRequest(int i) {
            SubscribeRequest request = new SubscribeRequest();

            request.setSubReqID(i);
            request.setUserName("liaozl");
            request.setProductName("Netty Book");
            request.setPhoneNumber("13430474856");
            request.setAddress("深圳市南山区沙尾东村");

            return request;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            for (int i = 0; i < 10; i++) {
                ctx.write(getRequest(i));
            }
            ctx.flush();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("Receive server response: " + msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

    }
}
