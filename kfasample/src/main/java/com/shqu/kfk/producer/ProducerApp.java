package com.shqu.kfk.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerApp {

	static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
	static final String TOPIC_NAME = "test";

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();

		props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		props.put("acks", "-1");
		props.put("reties", "3");
		props.put("batch.size", "12");
		props.put("linger.ms", 100000);
		props.put("buffer.memory", 33554432);
		//props.put("max.block.ms", 3000);

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 99999999; i++) {
			String key = "" + i, value = "NO." + i;
			 
			  producer.send( new ProducerRecord<String, String>(TOPIC_NAME, key, value),new Callback() {
				
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					System.out.println(metadata);
					
				}
			});
			//System.out.println(String.format("partition: %s, value: %s", result.get().partition(), value));

		}
		producer.close();

	}

}
