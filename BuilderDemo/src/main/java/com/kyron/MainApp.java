package com.kyron;

public class MainApp {

	public static void main(String[] args) {

		// buy all materials, the builder constructs in order
		House h = new HouseBuilder()
				.setDoor("front")
				.setRoof("shingles")
				.setFoundation("cement")
				.setWall("4")
				.build();
		
		System.out.println("House is completed.");

	}

}
