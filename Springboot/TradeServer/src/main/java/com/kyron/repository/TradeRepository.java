package com.kyron.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyron.domain.Trade;

public interface TradeRepository extends JpaRepository<Trade, Integer> {

}
