package com.liaozl.netty.demo.protocol.http.httpxml.encoder;

import com.liaozl.utils.xml.XmlPojoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author liaozuliang
 * @date 2016-12-13
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {

    protected ByteBuf encode(ChannelHandlerContext channelHandlerContext, Object body) throws Exception {
        String xmlStr = XmlPojoUtil.pojoToXml(body);
        System.out.println("encode from: " + body);
        System.out.println("encode to: " + xmlStr);

        ByteBuf buf = Unpooled.copiedBuffer(xmlStr, CharsetUtil.UTF_8);
        return buf;
    }

}
