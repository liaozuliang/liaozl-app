package com.liaozl.netty.demo.protocol.http.httpxml.decoder;

import com.liaozl.netty.demo.protocol.http.httpxml.pojo.HttpXmlResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-12-13
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse fullHttpResponse, List<Object> out) throws Exception {
        HttpXmlResponse response = new HttpXmlResponse(fullHttpResponse, super.decode(ctx, fullHttpResponse.content()));
        out.add(response);
    }
}
