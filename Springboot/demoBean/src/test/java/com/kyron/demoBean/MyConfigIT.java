package com.kyron.demoBean;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class MyConfigIT {
	
    private static final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
    	System.out.println("test 1");
        contextRunner.withUserConfiguration(MyConfig.class)
        	.run(context -> {
        		assertThat(context).hasBean("getName");       	
        	});
	}

}
