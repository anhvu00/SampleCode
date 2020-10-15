package com.kyron.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;


public class KafkaJsonDeserializer<T> implements Deserializer {

	private Class<T> type;
	
	/*
	 * Notice that in order to generalize the class to be used with different object types, 
	 * I’m using a constructor to pass the type
	 */
	public KafkaJsonDeserializer(Class type) {
		this.type = type;
	}

	@Override
	public void configure(Map map, boolean b) {
		// do nothing
	}

	@Override
	public Object deserialize(String s, byte[] bytes) {
		ObjectMapper mapper = new ObjectMapper();
		T obj = null;
		try {
			obj = mapper.readValue(bytes, type);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
		return obj;
	}

	@Override
	public void close() {
		// do nothing
	}
}