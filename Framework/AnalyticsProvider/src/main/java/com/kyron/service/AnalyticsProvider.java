package com.kyron.service;

import java.util.List;

public interface AnalyticsProvider {
	
	// Get ALL analytics types such as "FIND_ASSOCIATES", "COUNT_XXX", etc.
	public List<String> getAll();
	
	// TODO:
	public String analyze();

}
