package com.kyron.demoJson;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Configuration
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
	private String isLegacy;
	private String category; // RSS, XML, etc.
	private String optionalFlag; 
	private SourceAttributes[] attributes;
  
	
}
