package com.kyron.design;

public class MessageA extends Message {

	@Override
	public boolean save() {
		System.out.println("Message A saved");
		return true;
	}

}
