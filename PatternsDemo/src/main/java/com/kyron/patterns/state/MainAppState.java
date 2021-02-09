package com.kyron.patterns.state;

public class MainAppState {
	public static void main(String[] args) {
		TVContext context = new TVContext();
		IState tvStartState = new TVStartState();
		IState tvStopState = new TVStopState();
		
		context.setState(tvStartState);
		context.doAction();
		
		
		context.setState(tvStopState);
		context.doAction();
		
	}
}
