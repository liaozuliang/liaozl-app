package com.liaozl.rabbitmq.listener;

import com.liaozl.rabbitmq.converter.FastJsonMessageConverter;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liaozuliang
 * @date 2017-01-19
 */
@Service("noAutoAckListener")
public class NoAutoAckListener implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(NoAutoAckListener.class);

    @Resource
    private FastJsonMessageConverter fastJsonMessageConverter;

    private static final AtomicInteger times = new AtomicInteger(0);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String jsonMsg = fastJsonMessageConverter.getMessage(message);
            if (times.get() < 1 && jsonMsg.contains("DeadlockLoserDataAccessException")) {
                throw new DeadlockLoserDataAccessException("发生死锁", null);
            } else {
                System.out.println("执行业务，消费消息");
            }

            try {
                // 确认消息已经接收、消费;
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                logger.error("确认消息已经接收、消费出错：", e);
            }
        } catch (DeadlockLoserDataAccessException re) {
            try {
                // 如果是偶发性死锁异常，则让消息重新入队列进行重发。
                logger.error("发生死锁：", re);
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
                times.addAndGet(1);
            } catch (IOException e) {
                logger.error("发生死锁，消息重新入队列进行重发出错：", e);
            }
        } catch (Exception e) {
            logger.error("消费消息出错：", e);
            try {
                // 确认消息已经接收、消费;
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ie) {
                logger.error("确认消息已经接收、消费出错：", e);
            }
        }
    }
}
