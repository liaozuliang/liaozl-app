package com.liaozl.redis.pubsub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2017-02-09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-application.xml"})
public class RedisMessageSenderTest {

    @Resource
    private RedisMessageSender redisMessageSender;

    @Test
    public void testSend() {
        redisMessageSender.send("myReidsTopic", "test redis pub and sub");
        redisMessageSender.send("testChannel", "测试redis发布、订阅消息");
    }
}
