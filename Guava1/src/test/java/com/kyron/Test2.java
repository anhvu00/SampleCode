package com.kyron;

import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.kyron.event.BuyEvent;
import com.kyron.event.MultiHandlerSubscriber;
import com.kyron.event.ReturnEvent;
import com.kyron.event.SellEvent;

public class Test2 {

	// Test publish/subscribe
	@Test
	public void test() {
		// need a common eventBus
		EventBus ebus = new EventBus();
		// create a bunch of subscribers with the common bus
		MultiHandlerSubscriber msub = new MultiHandlerSubscriber(ebus);
		// create some events and post them, the subscribers will handle each
		BuyEvent b = new BuyEvent();
		ebus.post(b);
		SellEvent s = new SellEvent();
		ebus.post(s);
		assert true;
	}

	// Test un-handled/un-subscribed event
	@Test
	public void testUnhandle() {
		// need a common eventBus
		EventBus ebus = new EventBus();
		// create a bunch of subscribers with the common bus
		MultiHandlerSubscriber msub = new MultiHandlerSubscriber(ebus);
		// create some events and post them, the subscribers will handle each
		ReturnEvent r = new ReturnEvent();
		ebus.post(r);

	}

}
