package com.goldsentinel;

public class Car2 {
	private String color;
	private String type;
	private String data;

	public Car2() {
	}
	
	public Car2(String color, String type, String data) {
		this.color = color;
		this.type = type;
		this.data = data;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return ("color=" + this.color + "\ntype=" + this.type);
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
