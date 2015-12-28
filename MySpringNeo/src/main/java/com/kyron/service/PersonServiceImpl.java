package com.kyron.service;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.kyron.domain.Person;
import com.kyron.repository.PersonRepository;

@Service("personService")
public class PersonServiceImpl extends GenericService<Person> implements PersonService {

    @Autowired
    private PersonRepository personRepo;

    @Override
    public GraphRepository<Person> getRepository() {
        return personRepo;
    }
    
    public Person findName(String name) {
    	return personRepo.findByName(name);
    }
}
