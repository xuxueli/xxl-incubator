package com.xuxueli.demo.storm;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintBolt extends BaseRichBolt {
    public static Logger logger = LoggerFactory.getLogger(PrintBolt.class);

    OutputCollector _collector;

    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    public void execute(Tuple tuple) {
        String value = tuple.getString(0) + " Hello World!";

        _collector.ack(tuple);
        logger.info(">>>>>>>>>>> print bolt：{}", value);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}