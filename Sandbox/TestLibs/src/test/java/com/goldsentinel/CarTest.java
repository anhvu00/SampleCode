package com.goldsentinel;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CarTest {

	@Test
	public void testSerializeObjectToJson() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Car car = new Car("yellow", "renault");
		objectMapper.writeValue(new File("target/car.json"), car);
	}
	
	@Test
	public void testJsonToObject() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
		Car car = objectMapper.readValue(json, Car.class);  
		System.out.println(car.toString());
	}
	
	@Test
	public void testJsonNode() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{ \"color\" : \"Red\", \"type\" : \"FIAT\" }";
		JsonNode jsonNode = objectMapper.readTree(json);
		String color = jsonNode.get("color").asText();
		System.out.println("jsonNode color=" + color);
	}
	
	// this won't work b/c embedded data array...
	public void givenJsonOfArray_whenDeserializing_thenCorrect() 
	  throws JsonProcessingException, IOException {
	  
		String sampleData = "[{ \"id\" : \"ABC\", \"year\" : \"2011\" }, "
	      		+ "{ \"id\" : \"DBC\", \"year\" : \"2000\"}]";
	    String json
	      = "[{ \"color\" : \"Blue\", \"type\" : \"Honda\", \"data\" : " + sampleData + "}, "
	      		+ "{ \"color\" : \"Green\", \"type\" : \"Toyota\", \"data\" : " + sampleData + "}]";
	    
	    ObjectMapper mapper = new ObjectMapper();
	    List<Car2> list = mapper.reader()
	      .forType(new TypeReference<List<Car2>>() {})
	      .readValue(json);
	 
	    for (Car2 c : list) {
		    System.out.println(c.toString());	    	
	    }
	    System.out.println("arry test done");
	}

}
