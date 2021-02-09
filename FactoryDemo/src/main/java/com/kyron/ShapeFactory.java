package com.kyron;

public class ShapeFactory {
	   //use getShape method to get object of type shape 
	   public IShape getShape(String shapeType){
	      if(shapeType == null){
	         return null;
	      }	
	      
	      // TODO: use generics to avoid this case statement?
	      if(shapeType.equalsIgnoreCase("CIRCLE")){
	         return new Circle();
	         	         
	      } else if(shapeType.equalsIgnoreCase("SQUARE")){
	         return new Square();
	      }
	      
	      return null;
	   }
}
