package com.shqu.kfk.producer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionReplica;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Demonstrate how individual consumer works.
 * 
 * @author shqu
 * 
 */
public class ConsumerStandaloneApp {
	private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
	private static final String TOPIC_NAME = "test";

	public static void main(String[] args) throws Exception {

		new Thread(new ConsumerTask("task1")).start();
		// new Thread(new ConsumerTask("task2")).start();
	}

	private static class ConsumerTask implements Runnable {
		private String taskId;

		public ConsumerTask(String taskId) {
			this.taskId = taskId;
		}

		@Override
		public void run() {
			Properties props = new Properties();

			props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
			props.put("key.deserializer", StringDeserializer.class.getName());
			props.put("value.deserializer", StringDeserializer.class.getName());
			props.put("enable.auto.commit", "true");

			try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);) {
				List<PartitionInfo> partitionInfos = consumer.partitionsFor(TOPIC_NAME);

				List<TopicPartition> ps = partitionInfos.stream()
						.map(pi -> new TopicPartition(pi.topic(), pi.partition())).collect(Collectors.toList());
				consumer.assign(ps.subList(0, 1));

				while (true) {
					ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));

					Set<TopicPartition> partitions = records.partitions();

					for (TopicPartition tp : partitions) {

						List<ConsumerRecord<String, String>> partitionRecords = records.records(tp);

						for (ConsumerRecord<String, String> cr : partitionRecords) {
							System.out.println(String.format("task: %s, topic: %s, partition: %s, key: %s, value: %s",
									this.taskId, cr.topic(), cr.partition(), cr.key(), cr.value()));

						}

					}

				}
			}
		}
	}
}
