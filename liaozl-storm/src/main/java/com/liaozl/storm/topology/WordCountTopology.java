package com.liaozl.storm.topology;

import com.liaozl.storm.bolt.WordCountBolt;
import com.liaozl.storm.bolt.WordSplitBolt;
import com.liaozl.storm.spout.WordReadSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

/**
 * @author liaozuliang
 * @date 2017-02-25
 */
public class WordCountTopology {

    private static void start() {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader", new WordReadSpout());
        builder.setBolt("word-spliter", new WordSplitBolt()).shuffleGrouping("word-reader");
        builder.setBolt("word-counter", new WordCountBolt(), 1).fieldsGrouping("word-spliter", new Fields("wordChar"));

        Config config = new Config();
        config.put("times", 20);
        config.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("wordCountTopology", config, builder.createTopology());

        Utils.sleep(60 * 1000);
        cluster.killTopology("wordCountTopology");
        cluster.shutdown();

    }

    public static void main(String[] args) {
        start();
    }
}
