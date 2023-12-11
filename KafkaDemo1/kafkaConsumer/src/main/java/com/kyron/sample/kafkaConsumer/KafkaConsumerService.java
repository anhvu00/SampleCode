package com.kyron.sample.kafkaConsumer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Service
public class KafkaConsumerService {
    @Autowired
    MeterRegistry meterRegistry;

    private Consumer<String, String> kafkaConsumer;

    public void initializeKafkaConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "anh-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        // Set the maximum number of records to 1
//        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);

        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList("anh-topic"));
    }

    public void startConsuming() {
        if (kafkaConsumer == null) {
            throw new IllegalStateException("Kafka consumer not initialized. Call initializeKafkaConsumer() first.");
        }

        long startTime = System.currentTimeMillis();
        long durationInMinutes = 3;

        // Start consuming logic...
        while (System.currentTimeMillis() - startTime < durationInMinutes * 60 * 1000) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(5000));
            records.forEach(record -> {
                System.out.println("Received by api: " + record.value());
                TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                long lag = record.offset() - kafkaConsumer.position(topicPartition);
                System.out.println("   Lag for partition " + record.partition() + ": " + lag);
                // add time stamp to each exposed lag to see if prometheus can scrape more frequently
                meterRegistry.gauge("kafka_lag",
                        Tags.of("topic", record.topic(), "partition", String.valueOf(record.partition()),
                                "timestamp", String.valueOf(System.currentTimeMillis())), lag);

                // why prometheus doesn't show updated values for the following?
                meterRegistry.gauge("kafka_lag_single", Tags.of("topic", record.topic(), "partition", String.valueOf(record.partition())), lag);
            });
        }
    }

    public void consumeOne() {
        if (kafkaConsumer == null) {
            throw new IllegalStateException("Kafka consumer not initialized. Call initializeKafkaConsumer() first.");
        }

        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(15000));
        records.forEach(record -> {
            System.out.println("Received by api: " + record.value());
            TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
            long lag = record.offset() - kafkaConsumer.position(topicPartition);
            System.out.println("   Lag for partition " + record.partition() + ": " + lag);

            // why prometheus doesn't show updated values for the following?
            meterRegistry.gauge("kafka_lag_single", Tags.of("topic", record.topic(), "partition", String.valueOf(record.partition())), lag);
        });

    }

}

