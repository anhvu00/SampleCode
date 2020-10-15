package com.kyron.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// this class holds a json message
// will make it more complex with array and map later...
public class JsonMessage {

	@JsonProperty
	String item;
	@JsonProperty
	double price;
	@JsonProperty
	boolean available;
	
	// constructors
	public JsonMessage() {
		super();
	}
	
	public JsonMessage(String item, double price, boolean available) {
		this.available = available;
		this.item = item;
		this.price = price;
	}
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	@Override
	public String toString() {
		return "item:" + this.item + ", price:" + this.price + ", stock:" + this.available;
	}
}
