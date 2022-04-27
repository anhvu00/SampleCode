package com.kyron.BouncyCastleDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/")
public class SimpleController {

	@Autowired
	private DemoSSLConfig config;
	
	@GetMapping(path="/hello")
	public String hello() {
		return "hello";
	}
	
}
