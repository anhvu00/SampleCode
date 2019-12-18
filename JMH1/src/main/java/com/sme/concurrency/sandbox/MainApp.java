package com.sme.concurrency.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.*;

import com.sme.concurrency.sandbox.Person;

/**
 * The main() only initializes an input list and call the test methods.
 * Run MainApp as a standalone application shows you the time durations for each method.
 * This duration calculation is usually not accepted by the authorities.
 * So, we run them again using JMH in Average Time mode where the lower the score is the faster/better.
 * The default mode is Throughput where the higher the score, the better.
 * The annotations are for JMH.
 * @author anh
 * 12/17/19
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 6)
@Fork(value = 2)
public class MainApp {

	// define test parameters...
	// available processors
	public static final int CPUs = Runtime.getRuntime().availableProcessors();
	private static long startTime, endTime;

	// test data
	private static List<Person> peopleList = Arrays.asList(
			new Person("Andy", 10), 
			new Person("Andy", 20),
			new Person("Bob", 30), 
			new Person("Carol", 40), 
			new Person("Deedee", 50), 
			new Person("Eric", 25),
			new Person("Frank", 35), 
			new Person("Gene", 45));

	// TODO: does JMH work if the (static) methods are in another class?
	public static void main(String[] args) {
		System.out.println("CONCURRENCY DEMO MAIN APP\n");
		execSequential();
		execService();
		execConcurrent_Stream();
		execConcurrent_ParallelStream();
	}

	// TODO: does it work if the list is passed in as argument?
	public static void execSequential() {
		startTime = System.nanoTime();
		List<String> resultList = new ArrayList<String>();
		for (Person p : peopleList) {
			// find all prime numbers from 1 to a limit defined in Person class
			resultList.add(p.primeNumbersBruteForce());
		}
		endTime = System.nanoTime();
		System.out.println("Sequential duration = " + getDuration(startTime, endTime));
		System.out.println("-----------");
	}

	// TODO: break down create/shutdown to other methods and JMH them
	public static void execService() {
		startTime = System.nanoTime();
		List<String> resultList = null;
		// create exec service just once, using all available processors
		// consequential calls will be faster (not show in this example)
		ExecutorService executor = Executors.newFixedThreadPool(CPUs);

		// create input list for executor service
		// 2 ways to do this: with lambda function or a helper callable class
		ArrayList<Callable<String>> callableList = new ArrayList<Callable<String>>();
		for (Person p : peopleList) {
			callableList.add(() -> p.primeNumbersBruteForce());
		}

		// use executor to execute concurrently
		try {
			resultList = executor.invokeAll(callableList)
						.stream()
						.map(future -> {
							try {
								return future.get();
							} catch (Exception e) {
								throw new IllegalStateException(e);
							}
						}).collect(Collectors.toList());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		shutdownService(executor);	
		endTime = System.nanoTime();

		System.out.println("ExecutorService duration = " + getDuration(startTime, endTime));
		System.out.println("-----------");
	}

	public static void execConcurrent_Stream() {
		startTime = System.nanoTime();
		List<String> resultList = peopleList
				.stream()
				.map((p) -> p.primeNumbersBruteForce())
				.collect(Collectors.toList());
		endTime = System.nanoTime();
		System.out.println("Stream duration = " + getDuration(startTime, endTime));
		System.out.println("-----------");
	}

	public static void execConcurrent_ParallelStream() {
		startTime = System.nanoTime();
		List<String> resultList = peopleList
				.parallelStream()
				.map((p) -> p.primeNumbersBruteForce())
				.collect(Collectors.toList());
		endTime = System.nanoTime();
		System.out.println("Parallel stream duration = " + getDuration(startTime, endTime));
		System.out.println("-----------");
	}

	// helper function, underscore zeros to help reading
	private static long getDuration(long startTime, long endTime) {
		return (endTime - startTime) / 1_000_000;
	}
	
	// helper function
	private static void shutdownService(ExecutorService executor) {
		try {
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("Tasks interrupted");
		} finally {
			if (!executor.isTerminated()) {
				System.err.println("Cancel non-finished tasks");
			}
			executor.shutdownNow();
			//System.out.println("   Service is shut down.");
		}
	}
	
	// --------------------------------------------------------------------------------
	
	// Benchmark section - same functions, just remove print lines for benchmark to work
	
	@Benchmark
	public static void execSequential_bm() {
		List<String> resultList = new ArrayList<String>();
		for (Person p : peopleList) {
			// find all prime numbers from 1 to a limit defined in Person class
			resultList.add(p.primeNumbersBruteForce());
		}
	}

	@Benchmark
	public static void execService_bm() {

		List<String> resultList = null;
		// create exec service just once, using all available processors
		// consequential calls will be faster (not show in this example)
		ExecutorService executor = Executors.newFixedThreadPool(CPUs);


		// create input list for executor service
		// 2 ways to do this: with lambda function or a helper callable class
		ArrayList<Callable<String>> callableList = new ArrayList<Callable<String>>();
		for (Person p : peopleList) {
			callableList.add(() -> p.primeNumbersBruteForce());
		}

		/* use executor to execute concurrently. 
		 * invokeAll() returns a List<Future>. Must get() to retrieve String value.
		 * Note that we could use parallelStream instead of stream but it doesn't make a big different here.
		 */
		try {
			resultList = executor.invokeAll(callableList)
					.stream()
					.map(future -> {
						try {
							return future.get();
						} catch (Exception e) {
							throw new IllegalStateException(e);
						}
					}).collect(Collectors.toList());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		shutdownService(executor);	
	}

	@Benchmark
	public static void execConcurrent_Stream_bm() {
		List<String> resultList = peopleList
				.stream()
				.map((p) -> p.primeNumbersBruteForce())
				.collect(Collectors.toList());
	}

	@Benchmark
	public static void execConcurrent_ParallelStream_bm() {
		List<String> resultList = peopleList
				.parallelStream()
				.map((p) -> p.primeNumbersBruteForce())
				.collect(Collectors.toList());
	}


}
