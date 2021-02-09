package com.kyron.design;

public class MessageB extends Message {

	@Override
	public boolean save() {
		System.out.println("Message B saved");
		return true;
	}

}
