package com.kyron.sample.promReader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.search.MeterNotFoundException;

@RestController
@CrossOrigin
public class LagController {
//    @Autowired
//    private final MeterRegistry meterRegistry;

//    public LagController(MeterRegistry meterRegistry) {
//        this.meterRegistry = meterRegistry;
//    }

    @GetMapping("/hello")
    public String hello() {
        return ("hello from promReader");
    }
    /*
    @GetMapping("/lag")
    public String getLag() {
        try {
            // Retrieve the kafka_lag metric from Micrometer
            long kafkaLag = (long) meterRegistry.get("kafka_lag")
                    .tags(Tags.of("topic", "anh-topic", "partition", "0"))
                    .gauge()
                    .value();

            return "Current Kafka Lag: " + kafkaLag;
        } catch (MeterNotFoundException e) {
            return "Kafka Lag metric not found. Ensure the metric name and tags are correct.";
        }
    }*/
}


