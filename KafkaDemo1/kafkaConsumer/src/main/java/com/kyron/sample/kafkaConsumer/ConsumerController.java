package com.kyron.sample.kafkaConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Counter;

@RestController
@RequestMapping("/kafka")
public class ConsumerController {
    @Autowired
    KafkaConsumerService kafkaConsumerService;

    private Counter fakeLagCounter;

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

    // artificially increase lag to trigger autoscaling bc actual lag doesn't work yet
    @RequestMapping("/fake-counter-add/{incrementAmount}")
    public void incrementFakeLag(@PathVariable int incrementAmount) {
        // Increment fake lag by the specified amount, if negative, it does nothing
        kafkaConsumerService.increaseFakeCount(incrementAmount);
    }

    // test gauge for increase/decrease the counter
    @RequestMapping("/fake-gauge-update/{amount}")
    public void updateFakeGauge(@PathVariable int amount) {
        // Increment fake lag by the specified amount
        kafkaConsumerService.updateFakeGauge(amount);
    }


}
