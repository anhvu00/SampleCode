package com.kyron;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories("com.kyron.repository")
@EnableTransactionManagement
@ComponentScan("com.kyron")
public class AppConfig extends Neo4jConfiguration {

	// constructor
	public AppConfig() {
		super();
	}

	@Override
	public SessionFactory getSessionFactory() {
		return new SessionFactory("com.kyron.domain");
	}
}
