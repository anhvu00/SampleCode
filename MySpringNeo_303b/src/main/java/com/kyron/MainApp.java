package com.kyron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.kyron.domain.Person;
import com.kyron.repository.PersonRepository;

@SpringBootApplication
public class MainApp {

	@Component
	static class Runner implements CommandLineRunner {

		@Autowired
		private PersonRepository repo;

		@Override
		public void run(String... args) throws Exception {
			
			// count 
			long cnt = repo.count();
			if (cnt > 0) {
				System.out.println("Items count = " + cnt);
			} else {
				repo.createPersonConstraint("PERSON");
				System.out.println("Created constraint on PERSON name");
			}
			
			// tests
			testMerge(repo);
			testFindAll(repo);
			testLoadCsv(repo);
			testAddOne(repo,"ANH");
   		
		}

		public void testAddOne(PersonRepository repo, String name) {
			// find one
			Iterable<Person> list =  repo.findMe(name);
			if (list != null) {
				System.out.println(name + " Already in DB = NOT ADDED");
			} else {
				// add one
				Person somebody = new Person(name);
				repo.save(somebody);					
				System.out.println("ADDED " + name);
			}
		}
		
		public void testFindOne(PersonRepository repo, String name) {
			// find one
			Iterable<Person> list =  repo.findMe(name);
			if (list != null) {
				for( Person p : list) {
					System.out.println("my name is " + p.getName());
				}
			} else {
				System.out.println("NOT FOUND " + name);
			}
		}
		
		public void testFindAll(PersonRepository repo) {
			// find all
			Iterable<Person> list2 =  repo.findAll();
			if (list2 != null) {
				for( Person p : list2) {
					System.out.println("Name is " + p.getName());
				}
			} else {
				System.out.println("DATABASE EMPTY");
			}
		}
		
		public void testLoadCsv(PersonRepository repo) {
    		// test load csv from <db-dir>/import folder
    		String fname = "file:///people.csv";
    		int relcnt = repo.loadPeopleCsv(fname);
    		System.out.println("Imported " + relcnt + " relationships");
		}
		
		public void testMerge(PersonRepository repo) {
    		String fname = "file:///people.csv";
    		int relcnt = repo.loadCsvTest(fname); 
    		System.out.println("Imported " + relcnt + " nodes");
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Hello");
		SpringApplication.run(MainApp.class, args);
		System.out.println("Goodbye");
	}

} // end class
