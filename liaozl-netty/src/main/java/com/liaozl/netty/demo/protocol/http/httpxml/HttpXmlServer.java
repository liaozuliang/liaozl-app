package com.liaozl.netty.demo.protocol.http.httpxml;

import com.liaozl.netty.demo.protocol.http.httpxml.decoder.HttpXmlRequestDecoder;
import com.liaozl.netty.demo.protocol.http.httpxml.encoder.HttpXmlResponseEncoder;
import com.liaozl.netty.demo.protocol.http.httpxml.pojo.Address;
import com.liaozl.netty.demo.protocol.http.httpxml.pojo.HttpXmlRequest;
import com.liaozl.netty.demo.protocol.http.httpxml.pojo.HttpXmlResponse;
import com.liaozl.netty.demo.protocol.http.httpxml.pojo.Order;
import com.liaozl.netty.demo.protocol.http.httpxml.pojo.Shipping;
import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Arrays;

/**
 * @author liaozuliang
 * @date 2016-12-13
 */
public class HttpXmlServer {

    public static void main(String[] args) {
        new HttpXmlServer().bind(NettyConstant.PORT);
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
                    socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                    socketChannel.pipeline().addLast("xml-decoder", new HttpXmlRequestDecoder());
                    socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    socketChannel.pipeline().addLast("xml-encoder", new HttpXmlResponseEncoder());
                    socketChannel.pipeline().addLast("httpXmlServerHandler", new HttpXmlServerHandler());
                }
            });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("The Server is start in port: " + port);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {

        @Override
        protected void messageReceived(final ChannelHandlerContext ctx, HttpXmlRequest msg) throws Exception {
            HttpRequest request = msg.getRequest();

            Order order = (Order) msg.getBody();
            System.out.println("The Server receive request: " + order);

            doBussiness(order);

            ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null, order));
            if (!HttpHeaders.isKeepAlive(request)) {
                future.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        ctx.close();
                    }
                });
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            if (ctx.channel().isActive()) {
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }

        private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    status,
                    Unpooled.copiedBuffer("失败: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

        private void doBussiness(Order order) {
            order.setShipping(Shipping.INTERNATIONAL_EXPRESS);
            order.getCustomer().setFirstName("狄");
            order.getCustomer().setLastName("仁杰");
            order.getCustomer().setMiddleNames(Arrays.asList(new String[]{"李元芳", "武则天"}));

            Address address = new Address();
            address.setCountry("大唐");
            address.setState("洛阳");
            address.setCity("河南道");
            address.setStreet1("东知府");
            address.setStreet2("北门");
            address.setPostCode("960000");

            order.setShipTo(address);
            order.setBillTo(address);
        }
    }
}
