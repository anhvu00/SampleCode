package com.kyron;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.kyron.jaxb.Account;

public class Test1 {

	@Test
	public void test() {
		Account a = new Account();
		assertNotNull(a);
	}

}
