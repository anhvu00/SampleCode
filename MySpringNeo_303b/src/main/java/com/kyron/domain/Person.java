package com.kyron.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label="PERSON")
public class Person {
	@GraphId
	Long id;

	private String name;
	private int born;

	// constructor
	public Person() {
		super();
	}
	
	public Person(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getBorn() {
		return born;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBorn(int born) {
		this.born = born;
	}

}
