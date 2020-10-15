package com.kyron.kafka.dto;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserDeserializer implements Deserializer {
	@Override
	public void close() {
	}

	@Override
	public User deserialize(String arg0, byte[] arg1) {
		ObjectMapper mapper = new ObjectMapper();
		User user = null;
		try {
			user = mapper.readValue(arg1, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}