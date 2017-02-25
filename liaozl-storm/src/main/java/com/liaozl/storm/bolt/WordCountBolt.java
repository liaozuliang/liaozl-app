package com.liaozl.storm.bolt;

import org.apache.storm.shade.org.apache.commons.lang.math.NumberUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaozuliang
 * @date 2017-02-25
 */
public class WordCountBolt extends BaseRichBolt {

    private OutputCollector outputCollector;
    private int times;
    private Map<String, Integer> countMap = new HashMap<String, Integer>();

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.times = NumberUtils.toInt(map.get("times") + "", 10);
        this.outputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            //String wordChar = tuple.getString(0);
            //int count = tuple.getInteger(1);
            String wordChar = tuple.getStringByField("wordChar");

            int count = 1;
            if (countMap.containsKey(wordChar)) {
                count += countMap.get(wordChar);
            }

            countMap.put(wordChar, count);

            // 打印现在的统计数据
            int currentTimes = tuple.getIntegerByField("currentTimes");
            if (currentTimes == times) {
                System.out.println("-------统计数据(" + new Date().toLocaleString() + ")--------");
                for (String wc : countMap.keySet()) {
                    System.out.println("字符：" + wc + "，出现次数：" + countMap.get(wc));
                }
            }

            outputCollector.ack(tuple);
        } catch (Exception e) {
            e.printStackTrace();
            outputCollector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
