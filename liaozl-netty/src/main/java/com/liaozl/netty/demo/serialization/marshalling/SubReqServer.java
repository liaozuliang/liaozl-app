package com.liaozl.netty.demo.serialization.marshalling;

import com.liaozl.netty.demo.serialization.java.SubscribeRequest;
import com.liaozl.netty.demo.serialization.java.SubscribeResponse;
import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * JBoss Marshalling序列化
 *
 * @author liaozuliang
 * @date 2016-12-09
 */
public class SubReqServer {

    public static void main(String[] args) {
        new SubReqServer().bind(NettyConstant.PORT);
    }

    private void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, NettyConstant.BUFFER_SIZE);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                    socketChannel.pipeline().addLast(new SubReqServerChannelHandler());
                }
            });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("The SubReqServer is start in port: " + port);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    private class SubReqServerChannelHandler extends ChannelHandlerAdapter {

        private SubscribeResponse getResponse(int subReqID) {
            SubscribeResponse response = new SubscribeResponse();

            response.setSubReqID(subReqID);
            response.setRespCode(0);
            response.setDesc("Netty book order succeed, 3 days later, sent to the designated address");

            return response;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            SubscribeRequest request = (SubscribeRequest) msg;
            System.out.println("Receive client request: " + request);

            if ("liaozl".equals(request.getUserName())) {
                ctx.writeAndFlush(getResponse(request.getSubReqID()));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

    }
}
