package com.kyron.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
//import org.springframework.data.repository.CrudRepository;

import com.kyron.domain.Person;


public interface PersonRepository extends GraphRepository<Person> {

    Person findByName(String name);

    Iterable<Person> findByTeammatesName(String name);

}