package com.kyron.metricsDemo;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
@EnableScheduling
public class MyService {

  private final AtomicInteger testGauge;
  private final Counter testCounter;

  public MyService(MeterRegistry meterRegistry) {
    // Counter vs. gauge, summary vs. histogram
    // https://prometheus.io/docs/practices/instrumentation/#counter-vs-gauge-summary-vs-histogram
    testGauge = meterRegistry.gauge("custom_gauge", new AtomicInteger(0));
    testCounter = meterRegistry.counter("custom_counter");
  }

  @Scheduled(fixedRateString = "1000", initialDelayString = "0")
  public void schedulingTask() {
    testGauge.set(MyService.getRandomNumberInRange(0, 100));
    testCounter.increment();
    System.out.println("gauge=" + testGauge + ", counter=" + testCounter);
  }

  private static int getRandomNumberInRange(int min, int max) {
    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }
}