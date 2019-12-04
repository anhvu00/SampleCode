package com.kyron;

import com.kyron.service.AnalyticsService;
import com.kyron.service.AnalyticsProviderImpl;

public class MainApp {

	public static void main(String[] args) {
		System.out.println("Analytics Provider started.");
        AnalyticsService service = AnalyticsProviderImpl.getInstance().serviceImpl();
        System.out.println(service.getConfig("FIND_ASSOCIATES"));
        service.analyze("John Doe");
        
	}
	
	// services...
	public static void doit() {
		/*
		 * svc = new Service()
		 * config = svc.getAnalyticsConfig()
		 * typesList = svc.getTypes()
		 * status = svc.sendRequest(data)
		 * result = svc.getResponse()
		 */
	}

}
