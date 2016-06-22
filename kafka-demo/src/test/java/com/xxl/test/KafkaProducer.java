package com.xxl.test;

import java.text.MessageFormat;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * kafka
 *
 */
public class KafkaProducer {
	private static KafkaProducer instance = new KafkaProducer();
	public static Producer<String, String> getInstance(){
		return instance.producer;
	}
	
	private Producer<String, String> producer;
	private KafkaProducer() {
		Properties props = new Properties();
		
		//props.put("metadata.broker.list", "192.168.56.101:9092,192.168.56.101:9093,192.168.56.101:9094");			// 此处配置的是kafka的端口
		props.put("metadata.broker.list", "192.168.56.101:9092");
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");// 配置key的序列化类
		props.put("serializer.class", "kafka.serializer.StringEncoder");	// 配置value的序列化类
        props.put("request.required.acks","-1");							// 等待确认：0默认-不等待；1-等待leader确认；-1-等待所有存活确认；

        producer = new Producer<String, String>(new ProducerConfig(props));
	}
	
	public static void main(String[] args) {
		String TOPIC = "test";
		TOPIC = "my-replicated-topic";
		// push 2 topic
		for (int i = 1; i < 2; i++) {
			String key = "key".concat(String.valueOf(i));
			String value = "key".concat(String.valueOf(i));
			KafkaProducer.getInstance().send(new KeyedMessage<String, String>(TOPIC, key, value));
			System.out.println(MessageFormat.format("producer [{0} = {1}]", key, value));
		}
	}
	
}