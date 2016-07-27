package com.kyron.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.kyron.domain.Person;

@Repository
public interface PersonRepository extends GraphRepository<Person> {
    @Query("MATCH (p:PERSON {name:{0}}) RETURN p")
    Iterable<Person> findMe(String name);
}
