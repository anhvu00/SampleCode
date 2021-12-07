package com.kyron;

public class MainApp {

	public static void main(String[] args) {

		try {
			// missing foundation
			House h = new HouseBuilder()
					.setDoor("front")
					.setRoof("shingles")
					.setWall("4").build();
		} catch (IllegalArgumentException iae) {
			System.out.println("1. This house cannot be built because: " + iae.getMessage());
		}
		
		try {
			// missing wall
			House h = new HouseBuilder()
					.setDoor("front")
					.setRoof("shingles")
					.setFoundation("cement")
					.build();
		} catch (IllegalArgumentException iae) {
			System.out.println("2. This house cannot be built because: " + iae.getMessage());
		}

		// buy all materials, the builder constructs in order
		House h = new HouseBuilder()
				.setDoor("front")
				.setRoof("shingles")
				.setFoundation("cement")
				.setWall("4")
				.build();

		System.out.println("3. This house is built.");

	}

}
