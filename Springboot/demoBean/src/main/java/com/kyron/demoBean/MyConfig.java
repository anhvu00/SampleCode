package com.kyron.demoBean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MyConfig {
	
	@Bean
	public String getName() {
		return "Tony";
	}

}
