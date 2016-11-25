package com.liaozl.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author liaozuliang
 * @date 2016-11-02
 */
public class KafkaListener implements MessageListener<Integer, String> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaListener.class);

    @Override
    public void onMessage(ConsumerRecord<Integer, String> record) {
        String topic = record.topic();
        Integer key = record.key();
        String msg = record.value();

        System.out.println("receive a kafka msg[topic:" + topic + ", key:" + key + ", msg:" + msg + "]");
    }
}
