package com.kyron.service;

public class AnalyticsServiceImpl implements AnalyticsService {


	public String getConfig(String svcName) {
		// what to do?
		String retval = svcName + " configuration";
		return retval;
	}
	
	public void analyze(String author) {
        System.out.println("Analyzing "+author);		
	}

}

