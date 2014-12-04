package com.kyron.event;

public class ReturnEvent implements BaseEvent {
	String name;

	public ReturnEvent() {
		name = "Return event";
	}
	public String getName() {
		return name;
	}

}
