package com.kyron.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * POJO for serialize/de-serialize JSON record in kafka
 */

public class User {
	@JsonProperty
	private String name;
	@JsonProperty
	private int age;

	public User() {
		super();
	}

	public User(String firstname, int age) {
		this.name = firstname;
		this.age = age;
	}

	@Override
	public String toString() {
		return "User(" + getName() + ", " + getAge() + ")";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}