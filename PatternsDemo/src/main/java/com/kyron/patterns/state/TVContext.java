package com.kyron.patterns.state;

public class TVContext implements IState {

	private IState tvState;

	public void setState(IState state) {
		this.tvState=state;
	}

	public IState getState() {
		return this.tvState;
	}

	public void doAction() {
		this.tvState.doAction();
	}

}
