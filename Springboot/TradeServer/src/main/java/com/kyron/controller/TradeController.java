package com.kyron.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyron.domain.Trade;
import com.kyron.service.TradeService;

@RestController
@CrossOrigin
@RequestMapping(path="/trade") 
public class TradeController {
	
	@Autowired
	TradeService svc;
	
	@GetMapping("/getAll")
	public List<Trade> getAll() {
		return svc.getAllTrades();
	}
	

}
