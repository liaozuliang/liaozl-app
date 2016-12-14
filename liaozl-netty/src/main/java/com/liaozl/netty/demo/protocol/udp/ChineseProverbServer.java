package com.liaozl.netty.demo.protocol.udp;

import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

/**
 * @author liaozuliang
 * @date 2016-12-14
 */
public class ChineseProverbServer {

    public static void main(String[] args) {
        new ChineseProverbServer().run(NettyConstant.PORT);
    }

    private void run(int port) {
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup);
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.option(ChannelOption.SO_BROADCAST, true);
            bootstrap.handler(new ChineseProverbServerHandler());

            bootstrap.bind(port).sync().channel().closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        private final String[] DICTIONARY = {
                "只要功夫深，铁杵磨成针",
                "旧时王谢堂前燕，飞入寻常百姓家",
                "洛阳亲友如相问，一片冰心在玉壶",
                "一寸光阴一寸金，寸金难买寸光阴",
                "老骥伏枥，志在千里。烈士暮年，壮心不已！",
                ""
        };

        private String nextQuote() {
            int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
            return DICTIONARY[quoteId];
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
            String req = packet.content().toString(CharsetUtil.UTF_8);
            System.out.println(req);

            if ("谚语字典查询?".equals(req)) {
                ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语查询结果：" + nextQuote(), CharsetUtil.UTF_8), packet.sender()));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
