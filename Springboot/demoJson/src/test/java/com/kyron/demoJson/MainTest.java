package com.kyron.demoJson;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

class MainTest {

	@Test
	void testKirbyDs() {
		String fname = "kirby.json";
		String json = fileToKirby(fname);
		assertNotNull(json);
		System.out.println("**** TEST KIRBY.JSON TO JSON ****");
		System.out.println(json);
	}

	// @Test
	void testMasterDs() {
		String fname = "sampleDS.json";
		String json = fileToMasterDs(fname);
		assertNotNull(json);
		System.out.println(json);
	}

	@Test
	void testJolt() {

		// get resource file path
		String inputJson = getFilePath("sampleDS.json");
		String xformSpec = getFilePath("master2kirbySpec.json");
		System.out.println(inputJson);
		System.out.println(xformSpec);

		// if we know this is Legacy, transform with the xformSpec. If Kirby source,
		// then use another spec.

		// set file to chainr
		List chainrSpecJSON = JsonUtils.filepathToList(xformSpec);
		Chainr chainr = Chainr.fromSpec(chainrSpecJSON);

		Object inputJSON = JsonUtils.filepathToObject(inputJson);

		Object transformedOutput = chainr.transform(inputJSON);
		String retval = JsonUtils.toJsonString(transformedOutput);
		System.out.println(retval);

	}

	// test http call, get back a json
	@Test
	public void testHttpClient() {
		// create a client
		HttpClient client = HttpClient.newHttpClient();

		// Note the URI users/{id} might be different each run
		HttpRequest request = HttpRequest.newBuilder(
				URI.create("https://gorest.co.in/public/v1/users/91"))
				.header("accept", "application/json")
				.build();

		// use the client to send the request
		//var response = client.send(request, new JsonBodyHandler<>(Person.class));
		//System.out.println(response.body().get().title);
		try {
			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			// map response json string to object
			ObjectMapper mapper = new ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Person person = mapper.readValue((String) response.body(), Person.class);
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
			System.out.println(json);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	// helper functions should be in service.
	public String getFilePath(String fileName) {
		// get the path to input json file
		/*
		 * ClassLoader classLoader = getClass().getClassLoader(); File file = new
		 * File(classLoader.getResource(fileName).getFile()); String filePath =
		 * file.getAbsolutePath();
		 */

		String path = "src/main/resources/";

		File file = new File(path + fileName);
		String filePath = file.getAbsolutePath();
		if (file.exists()) {
			System.out.println(filePath + "Exists");
		} else {
			System.out.println(filePath + "Does not exist");
		}
		return filePath;
	}

	public String fileToMasterDs(String fileName) {
		String retval = "";
		// read from file
		InputStream is = readResourceFile(fileName);
		try {
			byte[] jsonData = is.readAllBytes();
			// create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			// convert json string to object
			MasterSource ms = objectMapper.readValue(jsonData, MasterSource.class);

			String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ms);

			retval = json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}

	public String fileToKirby(String fileName) {
		String retval = "";
		// read from file
		InputStream is = readResourceFile(fileName);
		try {
			byte[] jsonData = is.readAllBytes();
			// create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			;

			// convert json string to object
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
