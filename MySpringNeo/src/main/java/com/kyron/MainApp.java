package com.kyron;

import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.kyron.domain.Person;
import com.kyron.service.PersonServiceImpl;

// NOTE: implements CommandLineRunner...
@SpringBootApplication
public class MainApp {
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);
    
	/*
	 * The Configuration class extends Neo4jConfiguration.
	 * It also defines a scan path for all Neo4j repositories.
	 * scanner is Spring Data GemFire.
	 */
	@Configuration
	@EnableNeo4jRepositories(basePackages = "com.kyron")
	@EnableTransactionManagement
	static class ApplicationConfig extends Neo4jConfiguration {
	    private final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);		
		//static final String NEO_DB = "c:/users/anh/documents/neo4j/embed_1"; // current database
	    static final String NEO_DB = "c:/temp/neo4j_db1"; // current database

		// constructor
		public ApplicationConfig() {
			// do nothing
			log.debug("ApplicationConfig constructor...");
		}

		// new in 4.0
		
		@Override
		public Neo4jServer neo4jServer() {
	        log.info("Initialising server connection");
	        String user = "neo4j";
	        String password = "password";
	        RemoteServer rs = new RemoteServer("http://localhost:7474", user, password); 
	        return rs;
		}

		@Override
		public SessionFactory getSessionFactory() {
	        log.info("Initialising Session Factory");
	        return new SessionFactory("com.kyron");
		}
		
	}
	// ------- end configuration
	
    @Component
    static class Runner implements CommandLineRunner {
        @Autowired
        private PersonServiceImpl psi;

        @Override
        public void run(String... args) throws Exception {
            log.info(".... Fetching books");
    		log.info("In run...");
    		GraphRepository<Person> graphPersonRepo = psi.getRepository();
    		log.info("person count=" + graphPersonRepo.count() );
    		Person anh = psi.findName("ANH");
    		if (anh != null) {
        		log.info("my name is " + anh.name);
    		} else {
    			log.error("NOT FOUND ANH");
    		}
        }
    }


	
	public static void main(String[] args) throws Exception {
//		FileUtils.deleteRecursively(new File("accessingdataneo4j.db"));
		SpringApplication.run(MainApp.class, args);
		log.debug("DONE");
	}
}
