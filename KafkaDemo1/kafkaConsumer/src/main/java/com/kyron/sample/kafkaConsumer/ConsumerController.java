package com.kyron.sample.kafkaConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class ConsumerController {
    @Autowired
    KafkaConsumerService kafkaConsumerService;

    @RequestMapping("/hello")
    public String hello() {
        return ("hello from consumer controller");
    }

    @RequestMapping("/init")
    public void initializeKafkaConsumer() {
        System.out.println("initializing kafka consumer...");
        kafkaConsumerService.initializeKafkaConsumer();
    }

    public void startConsuming() {
        System.out.println("start consuming...");
        kafkaConsumerService.startConsuming();
    }

    @RequestMapping("/consumeAll")
    public void consumeAll() {
        initializeKafkaConsumer();
        startConsuming();
    }

//    @RequestMapping("/consumeOne")
//    public void consumeOne() {
//        kafkaConsumerService.consumeOne();
//    }

}
