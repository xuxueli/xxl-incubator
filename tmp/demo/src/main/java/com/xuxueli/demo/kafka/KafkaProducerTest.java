package com.xuxueli.demo.kafka;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * kafka producer
 *
 * @author xuxueli 2017-11-11
 */
public class KafkaProducerTest {
	public static void main(String[] args) {

		// build producer
		Properties props = new Properties();
		props.put("bootstrap.servers", "127.0.0.1:9092");        // 此处配置的是kafka的端口
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");        // 配置key的序列化类
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");    // 配置value的序列化类
		props.put("acks", "all");            // 等待确认：0 = 不等待/默认、1 = 等待leader确认、-1/all = 等待所有followers确认；
		props.put("retries", 1);

		Producer<String, String> producer = new KafkaProducer<String, String>(props);

		// pub msg
		String topic = "demo_topic";
		producer.send(new ProducerRecord<String, String>(topic, "Hello"));
		producer.send(new ProducerRecord<String, String>(topic, "World"), new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (e != null) {
					e.printStackTrace();
				} else {
					System.out.println("produce msg: offset = " + metadata.offset() + ", metadata = " + metadata.toString());
				}
			}
		});
		producer.flush();
		producer.close();

	}
}