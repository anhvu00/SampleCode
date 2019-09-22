package com.kyron.drools.model;

public class Message {
	public static final int HELLO = 0;
	public static final int GOODBYE = 1;
	private String message;
	private int status;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public static int getHello() {
		return HELLO;
	}
	public static int getGoodbye() {
		return GOODBYE;
	}

}
