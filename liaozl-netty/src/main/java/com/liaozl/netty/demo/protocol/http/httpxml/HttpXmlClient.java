package com.liaozl.netty.demo.protocol.http.httpxml;

import com.liaozl.netty.demo.protocol.http.httpxml.decoder.HttpXmlRequestDecoder;
import com.liaozl.netty.demo.protocol.http.httpxml.decoder.HttpXmlResponseDecoder;
import com.liaozl.netty.demo.protocol.http.httpxml.encoder.HttpXmlRequestEncoder;
import com.liaozl.netty.demo.protocol.http.httpxml.pojo.HttpXmlRequest;
import com.liaozl.netty.demo.protocol.http.httpxml.pojo.HttpXmlResponse;
import com.liaozl.netty.demo.protocol.http.httpxml.pojo.OrderFactory;
import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * @author liaozuliang
 * @date 2016-12-13
 */
public class HttpXmlClient {

    public static void main(String[] args) throws Exception {
        new HttpXmlClient().connect(NettyConstant.IP, NettyConstant.PORT);
    }

    private void connect(String host, int port) throws Exception {
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("http-decoder", new HttpResponseDecoder());
                    socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                    socketChannel.pipeline().addLast("xml-decoder", new HttpXmlResponseDecoder());
                    socketChannel.pipeline().addLast("http-encoder", new HttpRequestEncoder());
                    socketChannel.pipeline().addLast("xml-encoder", new HttpXmlRequestEncoder());
                    socketChannel.pipeline().addLast("httpXmlClientHandler", new HttpXmlClientHandler());
                }
            });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("client connect to server succeed.");

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.getOrder(123));
            ctx.writeAndFlush(request);
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
            System.out.println("The Client receive response of http header is: " + msg.getResponse().headers().names());
            System.out.println("The Client receive response ot http body is: " + msg.getResult());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

}