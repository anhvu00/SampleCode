package com.kyron;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Test1 {

	// Simple Guice injection test
	@Test
	public void test() {
		Injector i = Guice.createInjector(new GuiceModule());
		Wheel w = i.getInstance(Wheel.class);
		assertNotNull(w);
		System.out.println("Wheel tire and spoke are " + w.toString());
	}

}
