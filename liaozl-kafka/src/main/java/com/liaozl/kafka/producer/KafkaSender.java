package com.liaozl.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liaozuliang
 * @date 2016-11-02
 */
@Service("kafkaSender")
public class KafkaSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Resource
    private KafkaTemplate<Integer, String> kafkaTemplate;

    /**
     * 向默认的topic发送消息
     * @param msg
     */
    public void sendMsg(String msg) {
        try {
            kafkaTemplate.sendDefault(msg);
        } catch (Exception e) {
            logger.error("发送Kafka消息出错：", e);
        }
    }

    /**
     * 向指定的topic发送消息
     * @param topic
     * @param msg
     */
    public void sendMsg(String topic, Integer key, String msg) {
        try {
            kafkaTemplate.setDefaultTopic(topic);
            kafkaTemplate.sendDefault(key, msg);
        } catch (Exception e) {
            logger.error("发送Kafka消息出错：", e);
        }
    }

}
