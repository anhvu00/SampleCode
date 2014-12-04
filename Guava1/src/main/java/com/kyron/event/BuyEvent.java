package com.kyron.event;

public class BuyEvent implements BaseEvent {
	String name;

	public BuyEvent() {
		name = "Buy event";
	}
	public String getName() {
		return name;
	}

}
