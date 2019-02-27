package com.shqu.kfk.producer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerAutoCommitApp {
	private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
	private static final String TOPIC_NAME = "test";

	public static void main(String[] args) throws Exception {

		new Thread(new ConsumerTask("task1", "group1")).start();
		// new Thread(new ConsumerTask("task2", "group1")).start();
	}

	private static class ConsumerTask implements Runnable {
		private String taskId;
		private String groupId;

		public ConsumerTask(String taskId, String groupId) {
			this.taskId = taskId;
			this.groupId = groupId;
		}

		@Override
		public void run() {
			Properties props = new Properties();

			props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
			props.put("key.deserializer", StringDeserializer.class.getName());
			props.put("value.deserializer", StringDeserializer.class.getName());
			props.put("enable.auto.commit", "true");
			props.put("group.id", this.groupId);

			try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);) {
				consumer.subscribe(Arrays.asList(TOPIC_NAME));
				while (true) {
					ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000L));
					Set<TopicPartition> partitions = records.partitions();
					for (TopicPartition tp : partitions) {

						List<ConsumerRecord<String, String>> partitionRecords = records.records(tp);

						for (ConsumerRecord<String, String> cr : partitionRecords) {
							System.out.println(String.format("task: %s, topic: %s, partition: %s, key: %s, value: %s",
									this.taskId, cr.topic(), cr.partition(), cr.key(), cr.value()));

						}

					}

//					/**simple way to consume messages*/
//					
//						for (ConsumerRecord<String, String> cr : records) {
//							System.out.println(String.format("task: %s, topic: %s, partition: %s, key: %s, value: %s",
//									this.taskId, cr.topic(), cr.partition(), cr.key(), cr.value()));
//						}
//					}

				}
			}
		}
	}
}
