package com.kyron.demoJson;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Test httpclient 
 * https://gorest.co.in/public/v1/users/1922
 * {
   "meta":null,
   "data":{
      "id":1922,
      "name":"cdcdc",
      "email":"cddadcgfcc@gmail.com",
      "gender":"male",
      "status":"active"
   }
}
 */
@Getter @Setter @NoArgsConstructor
@Configuration
@JsonIgnoreProperties(ignoreUnknown = true)
class PersonData {
	private int id;
	private String name;
	private String email;
	private String gender;
	private String status;
}

@Getter @Setter @NoArgsConstructor
@Configuration
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
	private String meta;
	private PersonData data;

}
