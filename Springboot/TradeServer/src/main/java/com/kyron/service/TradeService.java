package com.kyron.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyron.domain.Trade;
import com.kyron.repository.TradeRepository;

@Service
public class TradeService {
	@Autowired
	TradeRepository tradeRepo;
	
	public List<Trade> getAllTrades() {
		return tradeRepo.findAll();
	}
}
