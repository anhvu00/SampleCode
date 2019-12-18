package com.sme.concurrency.sandbox;

import java.util.LinkedList;
import java.util.List;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * This helper class intends to:
 * - Perform special calculations needed to run concurrently
 * - Might be passed in as arguments to other methods. In order for JMH to track, JMH requires it to have @State annotation.
 * - Be replaced by your calculating class
 * 
 * @author anh
 * 12/17/19
 */
@State (Scope.Thread)
public class Person {
	// my limit to find prime number. Specific to this demo. 
	private static final int PRIME_LIMIT = 10000;
	
	private String name = "UNKNOWN";
	private int age = 0;

	// constructors - need an empty one for @State
	public Person() { }
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	// random calculation to demo concurrency and stream
	public int daysLived() {
		int retval = age * 365;
		return retval;
	}
	
	// another random calculation task
	public String primeNumbersBruteForce() {
		List<Integer> primeNumbers = new LinkedList<>();
		for (int i = 2; i <= PRIME_LIMIT; i++) {
			if (isPrimeBruteForce(i)) {
				primeNumbers.add(i);
			}
		}
		return primeNumbers.toString();
	}
	
	// same as above. overloading by taking an argument number as loop limit
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


}
