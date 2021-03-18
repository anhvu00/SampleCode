package com.kyron.metricsDemo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class MetricsController {
	
	@CrossOrigin
	@GetMapping("/hello")
	public String sayHello() {
		System.out.println("hello there");
		return("{name: anh}");
	}
	
	@GetMapping("/error")
	public String showError() {
		System.out.println("error here");
		return("{name: error}");
	}

}
