package com.kyron;

import com.kyron.AnalyticsProviderImpl;
import com.kyron.AnalyticsService;

/**
 * This class does the following:
 * - Query the AnalyticsProvider (through SPI) for a list of available engine names, objects (key-value pairs)
 * - Poll request queue for a request
 * - Send this request to the appropriate provider
 * - Update result
 *  
 * @author anh
 *
 */
public class AnalyticsEngineManager {
	
	// list all available engines
	public void getAll() {
        AnalyticsService service = FindAssociate.getInstance();
        System.out.println(service.getConfig("FIND_ASSOCIATES"));
		return;
	}

}
