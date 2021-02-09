package com.kyron.patterns.state;

public class TVStartState implements IState {

	public void doAction() {
		System.out.println("TV ON");
	}

}
