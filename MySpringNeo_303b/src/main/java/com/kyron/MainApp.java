package com.kyron;

import java.util.List;

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

			// find one
			Iterable<Person> list =  repo.findMe("ANH");
			if (list != null) {
				for( Person p : list) {
					System.out.println("my name is " + p.getName());
				}
			} else {
				System.out.println("NOT FOUND ANH");
			}
			
			// add one
			Person p = new Person("THUY");
			repo.save(p);
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Hello");
		SpringApplication.run(MainApp.class, args);
		System.out.println("Goodbye");
	}

} // end class
