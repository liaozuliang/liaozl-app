package com.liaozl.rabbitmq.producter;

import com.liaozl.rabbitmq.common.RabbitmqMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-09-29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class RabbitmqMsgProducterTest {

    @Resource
    private RabbitmqMsgProducter rabbitmqMsgProducter;

    @Test
    public void testNoAutoAckQueue() {
        try {
            RabbitmqMessage rm = new RabbitmqMessage();
            rm.setExchange("directExchange");
            rm.setRoutingKey("noAutoAckQueueKey");
            rm.setMsgBody("this is a directExchange rabbitmq msg, to noAutoAckQueue with DeadlockLoserDataAccessException");
            rabbitmqMsgProducter.sendMsg(rm);

            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDirectExchange() {
        try {
            RabbitmqMessage rm = new RabbitmqMessage();
            rm.setExchange("directExchange");
            rm.setRoutingKey("directQueueKey1");
            rm.setMsgBody("this is a directExchange rabbitmq msg");
            rabbitmqMsgProducter.sendMsg(rm);

            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDirectExchange2() {
        try {
            String exchange = "directExchange";
            String routingKey = "directQueueKey2";
            String msgBody = "this is a directExchange rabbitmq msg2";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFanoutExchange(){
        try {
            String exchange = "fanoutExchange";
            String routingKey = null;
            String msgBody = "this is a fanoutExchange rabbitmq msg";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTopicExchange(){
        try {
            String exchange = "topicExchange";
            String routingKey = "12QueueKey3";
            String msgBody = "this is a topicExchange rabbitmq msg";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "121.QueueKey3";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "23.aa.QueueKey3";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "ttopicQueue.2323";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topicQueueKey";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = ".QueueKey";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "121.QueueKey";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "121.bac.QueueKey";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topic..Key";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topic.adaf.Key";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topic.fadf.332.Key";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topicQueue.";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topicQueue.2";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topicQueue.a";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topicQueue.a232";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "topicQueue.afa.23";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTopicExchange2(){
        try {
            String exchange = "topicExchange2";
            String routingKey = "AAA";
            String msgBody = "this is a topicExchange rabbitmq msg";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA.";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA.1";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA.b";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA.23.c";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = ".AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "a.AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "10.AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "1.1.AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA..AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA.AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA.23.AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA.a.AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            routingKey = "AAA.232.232,.23.AAA";
            rabbitmqMsgProducter.sendMsg(exchange, routingKey, msgBody);

            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
