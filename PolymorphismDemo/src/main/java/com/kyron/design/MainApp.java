package com.kyron.design;

public class MainApp {

	// demo runtime polymorphism
	public static void main(String[] args) {
		Message genericMsg = new Message();
		Message msgA = new MessageA();
		Message msgB = new MessageB();
		
		System.out.println("Generic msg = " + genericMsg.save());
		System.out.println("A = " + msgA.save());
		System.out.println("B = " + msgB.save());

	}

}
