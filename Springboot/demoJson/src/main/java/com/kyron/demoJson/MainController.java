package com.kyron.demoJson;


import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@CrossOrigin
@RequestMapping(path="/") 
public class MainController {

	@Autowired
	private KirbyFeed kirbyDto;

	
	@GetMapping("/hello")
	public String hello(){
		return "Hello world";
	}
	
	@GetMapping("/kirbyconfig")
	public String getKirbyFeed() {
		return fileToKirbyDTO("kirby.json");
	}
	
	// helper functions should be in service.
	
	public String fileToKirbyDTO(String fileName) {
		String retval = "";
		// read from file
		InputStream is = readResourceFile(fileName);
		try {
			byte[] jsonData = is.readAllBytes();
			//create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();
			
			//convert json string to object
			kirbyDto = objectMapper.readValue(jsonData, KirbyFeed.class);
			
			String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(kirbyDto);
			System.out.println(json);
			
			retval = json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream readResourceFile(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        } else {
            return inputStream;
        }

    }

}
