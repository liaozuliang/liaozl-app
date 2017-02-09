package com.liaozl.redis.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 发布redis消息
 * @author liaozuliang
 * @date 2017-02-09
 */
@Service
public class RedisMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageSender.class);

    @Resource(name = "saveRedisTemplate")
    private RedisTemplate saveRedisTemplate;

    public void send(String channel, String message) {
        saveRedisTemplate.convertAndSend(channel, message);
        logger.info("Sended a message to channel:{}, the message is:{}", channel, message);
    }
}
