package com.liaozl.redis.pubsub;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author liaozuliang
 * @date 2017-02-09
 */
public class RedisMessageListenerTest {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("/spring-application.xml");

        while (true) { // 这里是一个死循环,目的就是让进程不退出,用于接收发布的消息
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
