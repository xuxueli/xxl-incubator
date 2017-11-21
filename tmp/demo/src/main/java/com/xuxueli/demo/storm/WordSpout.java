package com.xuxueli.demo.storm;

import org.apache.storm.topology.OutputFieldsDeclarer;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordSpout extends BaseRichSpout {
    public static Logger logger = LoggerFactory.getLogger(WordSpout.class);
    SpoutOutputCollector _collector;

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
    }

    public void nextTuple() {
        Utils.sleep(10 * 1000);
        final String[] words = new String[]{"nathan", "mike", "jackson", "golda", "bertels"};
        final String word = words[new Random().nextInt(words.length)];
        _collector.emit(new Values(word));
        logger.info(">>>>>>>>>>> word spout：{}", word);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));   // 发射数据-发射字段："emit.Values"-"declare.Fields" 需要保持一致
    }
}