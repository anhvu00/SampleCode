package com.kyron;

import com.google.inject.Inject;

// add comment, test git
public class Wheel {
	Tire t;
	Spoke s;

	// use GUICE to inject dependency tire and spoke
	@Inject
	public Wheel(Tire t, Spoke s) {
		this.t = t;
		this.s = s;
	}

	@Override
	public String toString() {
		return t.getName() + "," + s.getName();
	}
}
