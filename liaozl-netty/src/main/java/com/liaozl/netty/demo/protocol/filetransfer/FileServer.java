package com.liaozl.netty.demo.protocol.filetransfer;

import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author liaozuliang
 * @date 2016-12-14
 */
public class FileServer {

    public static void main(String[] args) {
        new FileServer().run(NettyConstant.PORT);
    }

    private void run(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 100);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(
                            new StringEncoder(CharsetUtil.UTF_8),
                            new LineBasedFrameDecoder(1024),
                            new StringDecoder(CharsetUtil.UTF_8),
                            new FileServerHandler()
                    );
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

    public class FileServerHandler extends SimpleChannelInboundHandler<String> {

        private final String CR = NettyConstant.LINE_SEPARATOR;

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
            String filePath = msg;

            File file = new File(filePath);
            if (file.exists()) {
                if (!file.isFile()) {
                    ctx.writeAndFlush("Not a file: " + file + CR);
                    return;
                }

                ctx.write(file + " " + file.length() + CR);

                RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
                FileRegion region = new DefaultFileRegion(
                        randomAccessFile.getChannel(),
                        0,
                        randomAccessFile.length()
                );

                ctx.write(region);
                ctx.writeAndFlush(CR);

                randomAccessFile.close();
            } else {
                ctx.writeAndFlush("File not found: " + file + CR);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
