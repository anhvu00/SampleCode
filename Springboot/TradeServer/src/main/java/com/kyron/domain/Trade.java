package com.kyron.domain;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trade implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="BROKER")
	private String broker;
	@Column(name="ACCOUNT")
	private String account;
	@Column(name="SYMBOL")
	private String symbol;
	@Column(name="QUANTITY")
	private int quantity;
	@Column(name="BUY_DATE")
	private String buyDate; // change to Date later...
	@Column(name="BUY_PRICE")
	private double buyPrice;
	@Column(name="BUY_TOTAL")
	private double buyTotal;
	@Column(name="SELL_DATE")
	private String sellDate; // change to Date later...
	@Column(name="SELL_QUANTITY")
	private double sellQuantity;
	@Column(name="SELL_PRICE")
	private double sellPrice;
	@Column(name="SELL_TOTAL")
	private double sellTotal;
	@Column(name="TARGET")
	private double target;
	@Column(name="GAIN_LOSS")
	private double gainLoss;
	@Column(name="GAIN_LOSS_PCT")
	private double gainLossPct;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBroker() {
		return broker;
	}
	public void setBroker(String broker) {
		this.broker = broker;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	public double getBuyTotal() {
		return buyTotal;
	}
	public void setBuyTotal(double buyTotal) {
		this.buyTotal = buyTotal;
	}
	public String getSellDate() {
		return sellDate;
	}
	public void setSellDate(String sellDate) {
		this.sellDate = sellDate;
	}
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	public double getSellTotal() {
		return sellTotal;
	}
	public void setSellTotal(double sellTotal) {
		this.sellTotal = sellTotal;
	}
	public double getTarget() {
		return target;
	}
	public void setTarget(double target) {
		this.target = target;
	}
	public double getGainLoss() {
		return gainLoss;
	}
	public void setGainLoss(double gainLoss) {
		this.gainLoss = gainLoss;
	}
	public double getGainLossPct() {
		return gainLossPct;
	}
	public void setGainLossPct(double gainLossPct) {
		this.gainLossPct = gainLossPct;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getSellQuantity() {
		return sellQuantity;
	}
	public void setSellQuantity(double sellQuantity) {
		this.sellQuantity = sellQuantity;
	}

}
