package com.liaozl.rabbitmq.listener;

import com.liaozl.rabbitmq.converter.FastJsonMessageConverter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Rabbitmq消息监听
 * @author liaozuliang
 * @date 2016-09-29
 */
public class RabbitmqMessageListener implements MessageListener {

    @Resource
    private FastJsonMessageConverter fastJsonMessageConverter;

    @Override
    public void onMessage(Message message) {
        String msgId = message.getMessageProperties().getMessageId();
        String msgExchange = message.getMessageProperties().getReceivedExchange();
        String msgRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        String consumerQueue = message.getMessageProperties().getConsumerQueue();

        String msgContent = fastJsonMessageConverter.getMessage(message);

        System.out.println("Receive Rabbitmq Msg: [id:" + msgId + ", exchange:" + msgExchange + ", routingKey:" + msgRoutingKey + ", consumerQueue:" + consumerQueue + ", content:" + msgContent + "]");
    }

}
