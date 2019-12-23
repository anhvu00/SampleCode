package com.kyron.concurrency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class MainTest {
	// test data
	public static final List<Person> peopleList = Arrays.asList(
			new Person("Andy", 10), 
			new Person("Andy", 20),
			new Person("Bob", 30), 
			new Person("Carol", 40), 
			new Person("Deedee", 50), 
			new Person("Eric", 25),
			new Person("Frank", 35), 
			new Person("Gene", 45));

	@Test
	public void launchBenchmark() throws Exception {

		Options opt = new OptionsBuilder()
				// Specify which benchmarks to run.
				// You can be more specific if you'd like to run only one benchmark per test.
				.include(this.getClass().getName() + ".*")
				// Set the following options as needed
				.mode(Mode.AverageTime)
				.timeUnit(TimeUnit.MICROSECONDS)
				.warmupTime(TimeValue.seconds(1))
				.warmupIterations(2)
				.measurementTime(TimeValue.seconds(1))
				.measurementIterations(2).threads(2).forks(1)
				.shouldFailOnError(true)
				.shouldDoGC(true)
				.build();

		new Runner(opt).run();
	}
	
	@Benchmark
	public static void execSequential_bm() {
		List<String> resultList = new ArrayList<String>();
		for (Person p : peopleList) {
			// find all prime numbers from 1 to a limit defined in Person class
			resultList.add(p.primeNumbersBruteForce());
		}
	}
	
	@Benchmark
	public void execParallelStream_bm() {
		List<String> resultList = peopleList
				.parallelStream()
				.map((p) -> p.primeNumbersBruteForce())
				.collect(Collectors.toList());
	}
	
	/*
	 * Use the following setup/test if you need Blackhole example
	 * See https://stackoverflow.com/questions/30485856/how-to-run-jmh-from-inside-junit-tests
	// The JMH samples are the best documentation for how to use it
	// http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
	@State(Scope.Thread)
	public static class BenchmarkState {
		List<Integer> list;
		@Setup(Level.Trial)
		public void initialize() {
			Random rand = new Random();
			list = new ArrayList<>();
			for (int i = 0; i < 1000; i++)
				list.add(rand.nextInt());
		}
	}

	@Benchmark
	public void benchmark1(BenchmarkState state, Blackhole bh) {
		List<Integer> list = state.list;
		for (int i = 0; i < 1000; i++)
			bh.consume(list.get(i));
	}
	*/

}
