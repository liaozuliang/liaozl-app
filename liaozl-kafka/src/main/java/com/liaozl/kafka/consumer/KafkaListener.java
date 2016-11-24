package com.liaozl.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author liaozuliang
 * @date 2016-11-02
 */
public class KafkaListener implements MessageListener<Integer, String> {

    private static final Logger logger = Logger.getLogger(KafkaListener.class);

    @Override
    public void onMessage(ConsumerRecord<Integer, String> record) {
        String topic = record.topic();
        Integer key = record.key();
        String msg = record.value();

        System.out.println("receive a kafka msg[topic:" + topic + ", key:" + key + ", msg:" + msg + "]");
    }
}
