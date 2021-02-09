package com.kyron;

public class MainApp {

	public static void main(String[] args) {
	      ShapeFactory shapeFactory = new ShapeFactory();
	      IShape shape1 = shapeFactory.getShape("");
	      shape1.draw();
	      
	      IShape square = shapeFactory.getShape("SQUARE");
	      square.draw();

	}

}
