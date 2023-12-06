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
    private final MeterRegistry meterRegistry;

    @Autowired
    public KafkaConsumerService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

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
     * This attempts to delay the consume, just 1 every 5 secs = unsuccessful
     */
    @PostConstruct
    public void consumeWithKafkaConsumerApi() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "anh-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("anh-topic"));

        long startTime = System.currentTimeMillis();
        long durationInMinutes = 3;

        while (System.currentTimeMillis() - startTime < durationInMinutes * 60 * 1000) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
            records.forEach(record -> {
                System.out.println("Received by api: " + record.value());
                TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                long lag = record.offset() - consumer.position(topicPartition);
                System.out.println("   Lag for partition " + record.partition() + ": " + lag);

                // Expose lag metrics to Prometheus
                meterRegistry.counter("kafka_lag", Tags.of("topic", record.topic(), "partition", String.valueOf(record.partition())))
                        .increment(lag);
            });
        }
    }


    /**
     * Modified version to force slower consumption
     * Don't know how to get the consumer to calc lag here.
     *
    @KafkaListener(topics = "anh-topic")
    public void consume(String message) {
        System.out.println("Received from listener: " + message);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */
}

