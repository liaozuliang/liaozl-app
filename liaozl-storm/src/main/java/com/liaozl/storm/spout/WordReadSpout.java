package com.liaozl.storm.spout;

import org.apache.storm.shade.org.apache.commons.lang.math.NumberUtils;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * @author liaozuliang
 * @date 2017-02-25
 */
public class WordReadSpout extends BaseRichSpout {

    private int times;
    private SpoutOutputCollector spoutOutputCollector;

    private static final String CHARS = "qwertyuiopasdfghjklzxcvbnm0123456789QWERTYUIOPASDFGHJKLZXCVBNM";

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.times = NumberUtils.toInt(map.get("times") + "", 10);
        this.spoutOutputCollector = spoutOutputCollector;
    }

    private static String getWord() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length() - 1)));
        }
        return sb.toString();
    }

    @Override
    public void nextTuple() {
        int i = 1;
        while (i <= times) {
            try {
                String word = getWord();
                spoutOutputCollector.emit(new Values(word, i));

                Thread.sleep(2 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            i++;
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word", "currentTimes"));
    }
}
