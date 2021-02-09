package com.kyron.design;

public class Message implements IMessage {

	public boolean save() {
		System.out.println("Message saved");
		return true;
	}

}
