package com.xxl.test;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

/**
 * kafka的topic数据流向：producer.topic --> (1:n as topic)group.id --> (1:1 as queue)consumer
 * @author xuxueli
 */
public class KafkaConsumer {
	private static KafkaConsumer instance = new KafkaConsumer();
	public static ConsumerConnector getInstance(){
		return instance.consumer;
	}

	private ConsumerConnector consumer;
	private KafkaConsumer() {
		Properties props = new Properties();
		props.put("zookeeper.connect", "192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183");	// zookeeper 配置
		props.put("group.id", "default-group");					// group 代表一个消费组
		
		props.put("zookeeper.session.timeout.ms", "4000");		// zk连接超时
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");
		
		props.put("serializer.class", "kafka.serializer.StringEncoder");	// 配置value的序列化类

		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
	}
	
	public static void main(String[] args) {
		String TOPIC = "test";
		TOPIC = "my-replicated-topic";
		// pull from topic
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(TOPIC, new Integer(1));

		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

		Map<String, List<KafkaStream<String, String>>> consumerMap = KafkaConsumer.getInstance().createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
		
		KafkaStream<String, String> stream = consumerMap.get(TOPIC).get(0);
		
		ConsumerIterator<String, String> it = stream.iterator();
		while (it.hasNext()){
			MessageAndMetadata<String, String> item = it.next();
			String key = item.key();
			String value = item.message();
			System.out.println(MessageFormat.format("consumer [{0} = {1}]", key, value));
		}
	}
}