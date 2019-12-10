package com.kyron;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;


/**
 * Demo basic concurrency and its performance. - Basic task is a calculation
 * (not use much memory/cpu) - Put them in a list - Create an ExecutorService
 * with max available CPUs/Processors - Invoke all in parallel, get back a list
 * of results - Print out duration of the sequential run and the concurrent run
 * - Print out result to see if they are in order.
 * 
 * @author anh
 *
 */
public class ConcurrencyDemo {

	// Available processors
	public static final int CPUs = Runtime.getRuntime().availableProcessors();
	// do each task this many times (more/less the number of processors)
	private static final int TASK_CYCLES = 12;
	// my limit to find prime number. Specific to this demo. 
	private static final int PRIME_LIMIT = 10000;
	private long startTime, endTime;

	// This is the calculation task. Replace it with your task.
	public String primeNumbersBruteForce(int n) {
		List<Integer> primeNumbers = new LinkedList<>();
		for (int i = 2; i <= n; i++) {
			if (isPrimeBruteForce(i)) {
				primeNumbers.add(i);
			}
		}
		return primeNumbers.toString();
	}

	public boolean isPrimeBruteForce(int number) {
		for (int i = 2; i < number; i++) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}

	// Benchmark time duration for sequential calls.
	public void sequentialDuration() {

		startTime = System.nanoTime();

		for (int i = 0; i < TASK_CYCLES; i++) {
			// find all prime numbers from 1 to 10,000
			primeNumbersBruteForce(PRIME_LIMIT);
		}

		endTime = System.nanoTime();

		System.out.println("Sequential duration = " + getDuration(startTime, endTime));
		System.out.println("------");
	}

	private long getDuration(long startTime, long endTime) {
		return (endTime - startTime) / 100_000;
	}

	/*
	 * Performance is about 6 times faster than sequential...
	 * TODO:
	 * - Try other threadPools
	 * - Try different CPUs number
	 */
	public void concurrentDuration() {
		// create exec service just once, using all available processors
		ExecutorService executor = Executors.newFixedThreadPool(CPUs);
		// 1st concurrency
		startTime = System.nanoTime();
		
		List<Future<String>> resultList = doInvokeAll(executor);
		
		endTime = System.nanoTime();

		// close exec service
		shutdownService(executor);
		
		System.out.println("Concurrent duration = " + getDuration(startTime, endTime));
		System.out.println("------");
		
		// (optional) print result to see if they are in order you expected.
		if (resultList != null) {
			System.out.println("Results in order:");
			for (Future<String> f : resultList) {
				String res = null;
				try {
					res = f.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				System.out.println(res);
			}
		} 
	}
	
	/*
	 * Wrapper Callable local class with ID so we can join them in order afterward.
	 * Replace this with your wrapper.
	 */
	class MyCallable implements Callable<String> {
		private final int ID;
		private StringBuilder msg = new StringBuilder();
		
		// constructor
		public MyCallable(int key) {
			this.ID = key;
		}
		
		@Override
		public String call() throws Exception {
			// pad leading zero to ID and add to the return value
			msg.append(StringUtils.leftPad(Integer.toString(ID), 2, "0"));
			msg.append("-");
			msg.append(primeNumbersBruteForce(PRIME_LIMIT));
			return msg.toString();
		}				
	}
	/*
	 * Input = Executor
	 * - Create a list of tasks using a wrapper Callable to check the order of the submitted tasks
	 * Output = list of Future (results from the tasks)
	 */
	public List<Future<String>> doInvokeAll(ExecutorService executor) {

		List<Future<String>> resultList = null;
		
		// create input list and run in parallel
		ArrayList<Callable<String>> callableList = new ArrayList<Callable<String>>();

		for (int i = 0; i < TASK_CYCLES; i++) {
			MyCallable c = new MyCallable(i);
			callableList.add(c);
		}

		// Executor invokeAll(inputList)
		try {
			resultList = executor.invokeAll(callableList);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	private void shutdownService(ExecutorService executor) {
		try {
			System.out.println("Shutting down executor");
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("Tasks interrupted");
		} finally {
			if (!executor.isTerminated()) {
				System.err.println("Cancel non-finished tasks");
			}
			executor.shutdownNow();
			System.out.println("Service is shut down.");
		}
	}
}
