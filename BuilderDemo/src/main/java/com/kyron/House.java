package com.kyron;

/**
 * The Builder is useful when:
 * 1. Require many parts and/or the logic of getting the parts are complex
 * 2. Construction order is important
 * 
 * @author anh
 *
 */
public class House {

	private String foundation;
	private String wall;
	private String roof;
	private String door;
	
	// constructor with a builder
	// The house can only be built in this order
	public House( HouseBuilder hb) throws IllegalArgumentException {
		// build order
		foundation = hb.getFoundation();
		if (null != foundation) {
			wall = hb.getWall();
			if (null != wall) {
				roof = hb.getRoof();
				door = hb.getDoor();
			} else {
				throw new IllegalArgumentException("Cannot place roof/door without the walls.");
			}
		} else {
			throw new IllegalArgumentException("Cannot build walls without the foundation.");
		}
	}
	
	// don't call this, call the constructor with a builder instead.
	public House build() {
		return this;
	}
	
	
	// setters/getters

	public String getFoundation() {
		return foundation;
	}

	public void setFoundation(String foundation) {
		this.foundation = foundation;
	}

	public String getWall() {
		return wall;
	}

	public void setWall(String wall) {
		this.wall = wall;
	}

	public String getRoof() {
		return roof;
	}

	public void setRoof(String roof) {
		this.roof = roof;
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	
}
