package com.kyron.sample.kafkaProducer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void produceOneMsg(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Produced message: " + message);
    }
    public void produceManyMsgs(String topic, String message) {
        // Produce messages every second for 2 minutes
        long endTime = System.currentTimeMillis() + 2 * 60 * 1000; // 2 minutes
        while (System.currentTimeMillis() < endTime) {
            // Create a message with a timestamp
            String msg = message + " at " + System.currentTimeMillis();
            // Send the message to the Kafka topic
            kafkaTemplate.send(topic, msg);
            System.out.println("Produced message: " + msg);
            // Sleep for one second
            try {
                Thread.sleep(1000); // 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
