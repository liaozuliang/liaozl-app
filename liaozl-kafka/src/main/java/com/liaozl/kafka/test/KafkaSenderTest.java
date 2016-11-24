package com.liaozl.kafka.test;

import com.liaozl.kafka.producer.KafkaSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-11-03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class KafkaSenderTest {

    @Resource
    private KafkaSender kafkaSender;

    @Test
    public void testSend1() {
        kafkaSender.sendMsg("test kafka send msg1");

        try {
            Thread.sleep(3 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSend2() {
        kafkaSender.sendMsg("testTopic2", 1, "测试一下kafka发送消息222222");

        try {
            Thread.sleep(3 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSend3() {
        kafkaSender.sendMsg("testTopic3", 3, "测试一下kafka发送消息333333");

        try {
            Thread.sleep(3 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
