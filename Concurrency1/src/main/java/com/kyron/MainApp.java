package com.kyron;

public class MainApp {

	public static void main(String[] args) {
		
		ConcurrencyDemo demo = new ConcurrencyDemo();
		demo.sequentialDuration();
		demo.concurrentDuration();
		
		System.out.println("THE END.");

	}

}
