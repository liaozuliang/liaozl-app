package com.liaozl.rabbitmq.producter;

import com.alibaba.fastjson.JSONObject;
import com.liaozl.rabbitmq.common.RabbitmqMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * rabbitmq消息发送器
 *
 * @author liaozuliang
 * @date 2016-09-29
 */
@Service("rabbitmqMsgProducter")
public class RabbitmqMsgProducter {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void sendMsg(String exchange, String routingKey, Object obj) {
        if (StringUtils.isBlank(exchange) || obj == null) {
            throw new IllegalArgumentException("参数错误，不能发送Rabbitmq消息");
        }

        amqpTemplate.convertAndSend(exchange, routingKey, JSONObject.toJSONString(obj));
    }

    public void sendMsg(RabbitmqMessage msg) {
        if (msg == null || StringUtils.isBlank(msg.getExchange()) || StringUtils.isBlank(msg.getMsgBody())) {
            throw new IllegalArgumentException("参数错误，不能发送Rabbitmq消息");
        }

        amqpTemplate.convertAndSend(msg.getExchange(), msg.getRoutingKey(), JSONObject.toJSONString(msg.getMsgBody()));
    }
}
