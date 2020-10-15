package com.kyron.kafka;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyron.kafka.dto.User;

public class KafkaJsonSerializer implements Serializer {

	/*
	 * Usage:
	 * 	static void runProducerSerialize() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		Producer<String, User> kafkaProducer = new KafkaProducer<>(props, new StringSerializer(),
				new KafkaJsonSerializer());
		// Send a message
		User user = new User();
		kafkaProducer.send(new ProducerRecord<>("TOPIC", "0", user));
	}
	 */
	
	@Override
	public void configure(Map map, boolean b) {
		// do nothing
	}

	@Override
	public byte[] serialize(String s, Object o) {
		byte[] retVal = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			retVal = objectMapper.writeValueAsBytes(o);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return retVal;
	}

	@Override
	public void close() {
		// do nothing
	}
}
