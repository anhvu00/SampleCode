package com.kyron.patterns.state;

public class TVStopState implements IState {

	public void doAction() {
		System.out.println("TV OFF");
	}

}
