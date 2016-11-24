/**
 * @Title: FastJsonMessageConverter.java
 * @Package com.niiwoo.common.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author seven
 * @date 2015-4-30 下午1:18:58
 * @version V1.0
 */


package com.liaozl.rabbitmq.converter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName: FastJsonMessageConverter
 * @Description: 消息序列化和反序列化类
 * @author seven
 * @date 2015-4-30 下午1:18:58
 *
 */

public class FastJsonMessageConverter extends AbstractMessageConverter {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private volatile String defaultCharset = DEFAULT_CHARSET;

    public FastJsonMessageConverter() {
        super();
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = (defaultCharset != null) ? defaultCharset
                : DEFAULT_CHARSET;
    }

    public Object fromMessage(Message message)
            throws MessageConversionException {
        return null;
    }

    public <T> T fromMessage(Message message, Class<T> clazz) {
        String json = "";
        try {
            json = new String(message.getBody(), defaultCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (T) JSONObject.parseObject(json, clazz);
    }

    public String getMessage(Message message) {
        String json = "";

        try {
            json = new String(message.getBody(),defaultCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return json;
    }

    protected Message createMessage(Object objectToConvert, MessageProperties messageProperties)
            throws MessageConversionException {
        byte[] bytes = null;

        try {
            String jsonString = JSONObject.toJSONString(objectToConvert);
            bytes = jsonString.getBytes(this.defaultCharset);
        } catch (UnsupportedEncodingException e) {
            throw new MessageConversionException(
                    "Failed to convert Message content", e);
        }

        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(this.defaultCharset);

        if (bytes != null) {
            messageProperties.setContentLength(bytes.length);
        }

        return new Message(bytes, messageProperties);
    }
}
