package com.kyron.event;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class MultiHandlerSubscriber {

	// constructor: needs a common eventBus
	public MultiHandlerSubscriber(EventBus eBus) {
		eBus.register(this); // register an Object
	}

	// now, subscribe everything...
	@Subscribe
	public void handleBuyEvent(BuyEvent e) {
		System.out.println("The event is " + e.getName());
	}

	@Subscribe
	public void handleSellEvent(SellEvent e) {
		System.out.println("The event is " + e.getName());
	}

	@Subscribe
	public void handleDeadEvent(DeadEvent dead) {
		System.out.println("The event is DEAD event.");
	}
}
