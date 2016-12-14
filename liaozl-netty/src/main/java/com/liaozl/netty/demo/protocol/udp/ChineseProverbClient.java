package com.liaozl.netty.demo.protocol.udp;

import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author liaozuliang
 * @date 2016-12-14
 */
public class ChineseProverbClient {

    public static void main(String[] args) {
        new ChineseProverbClient().run(NettyConstant.PORT);
    }

    private void run(int port) {
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup);
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.option(ChannelOption.SO_BROADCAST, true);
            bootstrap.handler(new ChineseProverbClientHandler());

            Channel ch = bootstrap.bind(0).sync().channel();

            // 向网段内的所有机器广播UDP消息
            ch.writeAndFlush(new DatagramPacket(
                            Unpooled.copiedBuffer("谚语字典查询?", CharsetUtil.UTF_8),
                            new InetSocketAddress("255.255.255.255", port)
                    )
            ).sync();

            if (!ch.closeFuture().await(15000)) {
                System.out.println("查询超时！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    public class ChineseProverbClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
            String response = packet.content().toString(CharsetUtil.UTF_8);
            if (response.startsWith("谚语查询结果：")) {
                System.out.println(response);
                ctx.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

    }
}
