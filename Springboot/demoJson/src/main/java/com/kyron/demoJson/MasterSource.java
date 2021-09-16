package com.kyron.demoJson;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
@Configuration
@JsonIgnoreProperties(ignoreUnknown = true)
public class MasterSource {
	
	private String id;
	private String uri;
	private String name;
	private String description;
	private String imageUri;
	private String state;
	private String updateFrequency;
	private String staleThreshold;
	private String reportingType;
	private QueryConfiguration queryConfiguration;
	
}
