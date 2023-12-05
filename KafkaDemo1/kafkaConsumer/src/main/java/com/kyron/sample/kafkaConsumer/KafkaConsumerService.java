package com.kyron.sample.kafkaConsumer;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Service
public class KafkaConsumerService {

    /*
     * Uncomment this and comment out the @PostConstruct method
     * to consume all messages immediately
     *
    @KafkaListener(topics = "anh-topic")
    public void consume(String message) {
        System.out.println("Received from listener: " + message);
    }
    */

    /**
     * Use the KafkaConsumer API directly.
     * This attempts to delay the consume, just 1 every 4 secs = unsuccessful
     *
    @PostConstruct
    public void consumeWithKafkaConsumerApi() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "anh-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("anh-topic"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(4000));
            records.forEach(record -> System.out.println("Received by api: " + record.value()));
        }
    }
    */

    /**
     * Modified version to force slower consumption
     */
    @KafkaListener(topics = "anh-topic")
    public void consume(String message) {
        System.out.println("Received from listener: " + message);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

