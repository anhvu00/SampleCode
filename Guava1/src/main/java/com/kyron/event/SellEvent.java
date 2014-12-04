package com.kyron.event;

public class SellEvent implements BaseEvent {
	String name;

	public SellEvent() {
		name = "Sell event";
	}

	public String getName() {
		return name;
	}

}
