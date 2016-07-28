package com.kyron.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.kyron.domain.Person;

@Repository
public interface PersonRepository extends GraphRepository<Person> {
    @Query("MATCH (p:PERSON {name:{0}}) RETURN p")
    Iterable<Person> findMe(String name);
    
    @Query("CREATE CONSTRAINT ON (n:PERSON) ASSERT n.name IS UNIQUE")
    void createPersonConstraint(String nodeLabel);
    
    @Query("MATCH (PERSON) return count(*) as cnt")
    long countPerson();
    
    /*
     * Sample data file. Note, no spaces after commas
     * PERSON_1,AGE_1,PERSON_2,AGE_2,
     * ANH,50,THUY,45,
     * DOAN,40,GIANG,60,
     */
    @Query("using periodic commit " 
    		+ "load csv with headers from {0} as csv "
    		+ "merge (p1:PERSON {name:csv.PERSON_1}) "
    	    + "on create set p1.name = csv.PERSON_1, p1.age = csv.AGE_1 "
    	    + "on match set p1.age = csv.AGE_1 "
    		+ "merge (p2:PERSON {name:csv.PERSON_2}) "
    	    + "on create set p2.name = csv.PERSON_2, p2.age = csv.AGE_2 "
    	    + "on match set p2.age = csv.AGE_2 "
    		+ "merge (p1)-[r:FRIEND]-(p2) "
    		+ "return count(r) as relCount;")    
    int loadPeopleCsv(String fname);    
    
    // test merge...
    @Query("using periodic commit " 
    		+ "load csv with headers from {0} as csv "
    		+ "merge (p1:PERSON {name:csv.PERSON_1}) "
    		+ "return count(p1) as relCount;")    
    int loadCsvTest(String fname);    

}
