package com.kyron.demoJson;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Configuration
public class QueryConfiguration {
	private String id;
	private String fork;
	private String dateFormat;
	private String workflowName;
	private String[] requestParameters;
	private String[] responseParameters;

}

/*
 * 	"queryConfiguration": {
		"id": "1295840",
		"fork": true,
		"dateFormat": "",
		"workflowName": "none",
		"requestParameters": [],
		"responseParameters": []
	},
 *
 */
