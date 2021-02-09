package com.kyron.patterns.strategy;

public class PaypalPayment implements IPaymentStrategy {

	private String emailId;
	private String password;
	
	public PaypalPayment(String email, String pwd){
		this.emailId=email;
		this.password=pwd;
	}
	
	public void pay(int amount) {
		System.out.println(amount + " paid using Paypal.");
	}

}
