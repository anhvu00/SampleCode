package com.kyron.sample.kafkaProducer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KafkaProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerApplication.class, args);
	}

	@Bean
	public CommandLineRunner cmdWriteKMsg(KafkaProducerService kps) {
		return args -> {
//			kps.produceOneMsg("anh-topic", "hello there");
			kps.produceManyMsgs("anh-topic", "hello there");
		};
	}

}
