package com.liaozl.netty.demo.protocol.http.httpxml.decoder;

import com.liaozl.utils.xml.XmlPojoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

/**
 * @author liaozuliang
 * @date 2016-12-13
 */
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {

    protected Object decode(ChannelHandlerContext ctx, ByteBuf body) throws Exception {
        String content = body.toString(CharsetUtil.UTF_8);
        Object object = XmlPojoUtil.xmlToPojo(content);

        System.out.println("decode from: " + content);
        System.out.println("decdode to: " + object);

        return object;
    }
}
