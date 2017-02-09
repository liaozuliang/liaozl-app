package com.liaozl.redis.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * 监听redis消息
 * @author liaozuliang
 * @date 2017-02-09
 */
public class RedisMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    @Resource(name = "queryRedisTemplate")
    private RedisTemplate queryRedisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = (String) queryRedisTemplate.getValueSerializer().deserialize(message.getChannel());
        String redisMsg = (String) queryRedisTemplate.getValueSerializer().deserialize(message.getBody());
        logger.info("Received a message from channel:{}, the message is:{}", channel, redisMsg);
    }
}
