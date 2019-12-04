package com.kyron.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import com.kyron.service.AnalyticsService;

public class AnalyticsProviderImpl implements AnalyticsProvider {

	private static AnalyticsProviderImpl provider;
	private ServiceLoader<AnalyticsService> loader;

	// Constructor - Singleton pattern
	private AnalyticsProviderImpl() {
		loader = ServiceLoader.load(AnalyticsService.class);
	}

	public static AnalyticsProviderImpl getInstance() {
		if (provider == null) {
			provider = new AnalyticsProviderImpl();
		}
		return provider;
	}

	// return first available service
	// TODO: filter/search for a particular one by name
	public AnalyticsService serviceImpl() {
		AnalyticsService service = loader.iterator().next();
		if (service != null) {
			return service;
		} else {
			throw new RuntimeException("No implementation for AnalyticProviderImpl");
		}
	}

	// return a list of all available services
	public List<String> getAll() {
		ArrayList<String> l = new ArrayList<String>();
		l.add("service1");
		l.add("service2");
		l.add("service3");
		return l;
	}

	public String analyze() {
		// TODO Auto-generated method stub
		return null;
	}

}
