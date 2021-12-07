package com.kyron;

public class HouseBuilder {
	
	private String foundation;
	private String wall;
	private String roof;
	private String door;
	
	public House build() {
		return new House(this);
	}
	
	// setters: pretend you have to do a lot more
	// like lookup for the cheapest price, set color, etc.
	public HouseBuilder setFoundation(String foundation) {
		this.foundation = foundation;
		return this;
	}
	public HouseBuilder setWall(String wall) {
		this.wall = wall;
		return this;
	}

	public HouseBuilder setRoof(String roof) {
		this.roof = roof;
		return this;
	}

	public HouseBuilder setDoor(String door) {
		this.door = door;
		return this;
	}
	
	// getters
	public String getFoundation() {
		return foundation;
	}
	public String getWall() {
		return wall;
	}
	public String getRoof() {
		return roof;
	}
	public String getDoor() {
		return door;
	}

	
	

}
