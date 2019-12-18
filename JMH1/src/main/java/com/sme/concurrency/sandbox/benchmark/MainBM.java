package com.sme.concurrency.sandbox.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.sme.concurrency.sandbox.MainApp;

public class MainBM {
    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder()
                .include(MainApp.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(options).run();
    }
}
