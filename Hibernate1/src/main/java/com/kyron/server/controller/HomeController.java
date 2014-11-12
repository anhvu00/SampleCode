package com.kyron.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kyron.server.data.User;
import com.kyron.server.data.UserRepository;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepo;

	protected static final String HOME_VIEW = "home";

	@RequestMapping("/hello/{name}")
	public String home(@PathVariable("name") String s, ModelMap model) {
		model.addAttribute("output", s);
		System.out.println("param=" + s);
		return HOME_VIEW;
	}

	@RequestMapping(value = "/user")
	public String getUser(ModelMap model) {
		User u = userRepo.findOne(1L);
		model.addAttribute("output", u.getEmail());
		return HOME_VIEW;
	}

}
