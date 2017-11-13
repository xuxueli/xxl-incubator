package com.xuxueli.demo.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

public class ExclamationTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("word", new TestWordSpout(), 1);
        builder.setBolt("exclaim", new ExclamationBolt(), 1).shuffleGrouping("word");
        builder.setBolt("print", new PrintBolt(), 1).shuffleGrouping("exclaim");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        } else {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("test3", conf, builder.createTopology());
            Utils.sleep(20000);
            cluster.killTopology("test3");
            cluster.shutdown();
        }
    }
}