package com.kyron;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.kyron.AnalyticsService;

public class FindAssociate implements AnalyticsProvider {

	private static FindAssociate provider;
	private ServiceLoader<AnalyticsService> loader;

	// Constructor - Singleton pattern
	private FindAssociate() {
		loader = ServiceLoader.load(AnalyticsService.class);
	}

	public static synchronized FindAssociate getInstance() {
		if (provider == null) {
			provider = new FindAssociate();
		}
		return provider;
	}

	public String analyze(String name) {
        String retval = null;

        try {
            Iterator<AnalyticsService> dictionaries = loader.iterator();
            while (retval == null && dictionaries.hasNext()) {
            	AnalyticsService d = dictionaries.next();
                d.analyze(name);
                retval = "DONE";
            }
        } catch (ServiceConfigurationError serviceError) {
            retval = null;
            serviceError.printStackTrace();

        }
        return retval;
	}

	public String getType() {
		return "FIND-ASSOCIATE";
	}

}
