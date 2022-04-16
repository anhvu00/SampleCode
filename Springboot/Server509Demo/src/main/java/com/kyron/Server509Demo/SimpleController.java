package com.kyron.Server509Demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/")
public class SimpleController {

	@GetMapping(path="/hello")
	public String hello() {
		return "hello";
	}
}
