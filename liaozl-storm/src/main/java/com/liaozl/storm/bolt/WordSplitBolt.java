package com.liaozl.storm.bolt;

import org.apache.storm.shade.org.apache.commons.lang.math.NumberUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liaozuliang
 * @date 2017-02-25
 */
public class WordSplitBolt extends BaseRichBolt {

    private OutputCollector outputCollector;
    private List<String> wordList = new ArrayList<String>();
    private int times;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.times = NumberUtils.toInt(map.get("times") + "", 10);
        this.outputCollector = outputCollector;

    }

    @Override
    public void execute(Tuple tuple) {
        try {
            //String word = tuple.getString(0);
            String word = tuple.getStringByField("word");
            wordList.add(word);

            int currentTimes = tuple.getIntegerByField("currentTimes");
            if (currentTimes == times) {
                System.out.println("-------所有单词-------");
                for (String str : wordList) {
                    System.out.println(str);
                }
            }

            for (char c : word.toLowerCase().toCharArray()) {
                outputCollector.emit(new Values(c + "", currentTimes));
            }

            outputCollector.ack(tuple);
        } catch (Exception e) {
            e.printStackTrace();
            outputCollector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("wordChar", "currentTimes"));
    }
}