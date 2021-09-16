package com.kyron.demoJson;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// private class - will be moved to its own file
@Getter @Setter @NoArgsConstructor
class SourceAttributes {
	private String id;
	private String storedName;
	private String regex;
	private int priority;
}

@Getter @Setter @NoArgsConstructor
class ProcConfig {
	private String strategy;
	private IngestParam[] params;
}

@Getter @Setter @NoArgsConstructor
class Filter {
	public String description;
	public IngestParam[] params;
}

@Getter @Setter @NoArgsConstructor
class FilterConfig {
	Filter filenameFilter;
	Filter pubDateFilter;
	Filter contentFilter;	
}

@Getter @Setter @NoArgsConstructor
@Configuration
@JsonIgnoreProperties(ignoreUnknown = true)
public class KirbyFeed {
	
	private int sourceId;
	private String sourceUri;
	private String sourceName;
	private String sourceDescription;
	private String state;
	private String updateFrequency;
	private String staleThreshold;
	private String imageUri;
	private String workflowName;
	private boolean isLegacy;
	private String category; // RSS, XML, etc.
	private String optionalFlag; 
	private SourceAttributes[] attributes;
	private ProcConfig fetchConfig;
	private ProcConfig parseConfig;
	private FilterConfig filterConfig;  // contain 1 - 3 different filters (fileName, content, date)
  
	
}
