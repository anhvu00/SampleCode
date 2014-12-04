package com.kyron;

import com.google.inject.AbstractModule;

// single place to tell GUICE how to bind
public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		// dont need .to(), it's smart enough to know which
		bind(Tire.class);
		bind(Spoke.class);

	}

}
