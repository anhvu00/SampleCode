package com.kyron.demoJson;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class MainTest {

	@Test
	void test() {
		String fname = "kirby.json";
		String json = fileToKirby(fname);
		assertNotNull(json);
		System.out.println(json);		
	}
	
	public String fileToKirby(String fileName) {
		String retval = "";
		// read from file
		InputStream is = readResourceFile(fileName);
		try {
			byte[] jsonData = is.readAllBytes();
			//create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();
			
			//convert json string to object
			KirbyFeed kf = objectMapper.readValue(jsonData, KirbyFeed.class);
			
			String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(kf);
			
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
