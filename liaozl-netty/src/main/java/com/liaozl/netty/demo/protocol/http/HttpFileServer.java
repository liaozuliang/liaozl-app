package com.liaozl.netty.demo.protocol.http;

import com.liaozl.netty.util.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;

/**
 * @author liaozuliang
 * @date 2016-12-12
 */
public class HttpFileServer {

    private static final String DEFAULT_URL = "/";

    public static void main(String[] args) throws Exception {
        new HttpFileServer().run(NettyConstant.PORT, DEFAULT_URL);
    }

    private void run(final int port, final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                    socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    socketChannel.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                }
            });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("HTTP文件目录服务器启动成功， 网址是：http://localhost:" + port + url);

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

        private final String url;

        public HttpFileServerHandler(String url) {
            this.url = url;
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
            if (!request.getDecoderResult().isSuccess()) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                return;
            }

            if (request.getMethod() != HttpMethod.GET) {
                sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
                return;
            }

            final String uri = request.getUri();
            final String path = sanitizeUri(uri);
            if (path == null) {
                sendError(ctx, HttpResponseStatus.FORBIDDEN);
                return;
            }

            File file = new File(path);
            if (file.isHidden() || !file.exists()) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }

            if (file.isDirectory()) {
                if (uri.endsWith("/")) {
                    sendListing(ctx, file);
                } else {
                    sendRedirect(ctx, uri + "/");
                }
                return;
            }

            if (!file.isFile()) {
                sendError(ctx, HttpResponseStatus.FORBIDDEN);
                return;
            }

            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(file, "r");
            } catch (Exception e) {
                e.printStackTrace();
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }

            long fileLength = randomAccessFile.length();
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            setContentLength(response, fileLength);
            setContentTypeHeader(response, file);

            if (isKeepAlive(request)) {
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }

            ctx.write(response);

            ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
            sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                @Override
                public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long progress, long total) throws Exception {
                    if (total < 0) {
                        System.out.println("Transfer progress: " + progress);
                    } else {
                        System.out.println("Transfer progress: " + progress + "/" + total);
                    }
                }

                @Override
                public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                    System.out.println("Transfer Complete.");
                }
            });

            ChannelFuture lastContectFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!isKeepAlive(request)) {
                lastContectFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            if (ctx.channel().isActive()) {
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }

        private void setContentTypeHeader(HttpResponse response, File file) {
            MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file));
        }

        private final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
        private final String LI_TEMP = "<li>链接：<a href=\"{0}/\">{1}</a></li>\r\n";

        private void sendListing(ChannelHandlerContext ctx, File dir) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");

            String dirPath = dir.getPath();

            StringBuffer sb = new StringBuffer();
            sb.append("<!DOCTYPE html>\r\n");
            sb.append("<html><head><title>");
            sb.append(dirPath);
            sb.append(" 目录：");
            sb.append("</title></head><body>\r\n");
            sb.append("<h3>");
            sb.append(dirPath).append(" 目录：");
            sb.append("</h3>\r\n");
            sb.append("<ul>");
            sb.append(MessageFormat.format(LI_TEMP, "../", ".."));

            for (File f : dir.listFiles()) {
                if (f.isHidden() || !f.canRead()) {
                    continue;
                }

                String name = f.getName();
                if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                    continue;
                }

                sb.append(MessageFormat.format(LI_TEMP, name, name));
            }

            sb.append("</ul></body></html>\r\n");

            ByteBuf buf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();

            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

        private void sendRedirect(ChannelHandlerContext ctx, String newUri) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.LOCATION, newUri);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

        private final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

        private String sanitizeUri(String uri) {
            try {
                uri = URLDecoder.decode(uri, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                try {
                    uri = URLDecoder.decode(uri, "ISO-8859-1");
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                    throw new Error();
                }
                e.printStackTrace();
            }

            if (!uri.startsWith(url)) {
                return null;
            }

            if (!uri.startsWith("/")) {
                return null;
            }

            uri = uri.replace('/', File.separatorChar);
            if (uri.contains(File.separator + '.')
                    || uri.contains('.' + File.separator)
                    || uri.startsWith(".")
                    || url.endsWith(".")
                    || INSECURE_URI.matcher(uri).matches()
                    ) {
                return null;
            }

            return System.getProperty("user.dir") + File.separator + uri;
        }

        private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    status,
                    Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}

