package com.xuxueli.demo.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * kafka consumer （producer.topic --> (1:n as topic)group.id --> (1:1 as queue)consumer）
 *
 * @author xuxueli 2017-11-11
 */
public class KafkaConsumerTest {

	public static void main(String[] args) {

		// build consumer
		Properties props = new Properties();
		props.put("bootstrap.servers", "127.0.0.1:9092");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");		// 配置value的序列化类
		props.setProperty("group.id", "0");				// group 代表一个消费组（可根据之，实现queue队列或者topic广播）
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.offset.reset", "earliest");

		Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		// sub msg
		String topic = "demo_topic";
		consumer.subscribe(Arrays.asList(topic));

		for (int i = 0; i < 100; i++) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			System.out.println(records.count());
			for (ConsumerRecord<String, String> record : records) {
				System.out.println("record = " + record);
			}
		}

		//  close consumer
		consumer.close();

	}
}