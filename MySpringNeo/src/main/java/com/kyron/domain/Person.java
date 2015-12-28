package com.kyron.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label="PERSON")
public class Person extends Entity {

	@GraphId
	Long id;
	public String name;

	// default constructor - required.
	public Person() {
		// do nothing
	}

	// constructor
	public Person(String name) {
		this.name = name;
	}

	// Fetch = eager load. Otherwise you would have to use neo4jTemplate.fetch()
	@Relationship(type = "TEAMMATE")
	public Set<Person> teammates; 

	// establish relationship...
	public void worksWith(Person person) {
		if (teammates == null) {
			teammates = new HashSet<Person>();
		}
		teammates.add(person);
	}

	public String toString() {
		String results = name + "'s teammates include\n";
		if (teammates != null) {
			for (Person person : teammates) {
				results += "\t- " + person.name + "\n";
			}
		}
		return results;
	}

}
