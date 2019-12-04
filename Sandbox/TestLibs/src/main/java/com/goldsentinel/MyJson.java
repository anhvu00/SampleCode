package com.goldsentinel;

import java.io.StringReader;
import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import com.fasterxml.jackson.databind.util.JSONPObject;

public class MyJson {
	
	public static String jsonString = "{\"test1\":\"value1\",\"test2\":{\"id\":0,\"name\":\"testName\"}}";

	public void createOne() {
		// Create Json and print
		JsonObject json = Json.createObjectBuilder().add("name", "Falco").add("age", BigDecimal.valueOf(3))
				.add("biteable", Boolean.FALSE).build();
		String result = json.toString();

		System.out.println(result);

		// Read back
		JsonReader jsonReader = Json.createReader(new StringReader("{\"name\":\"Falco\",\"age\":3,\"bitable\":false}"));
		JsonObject jobj = jsonReader.readObject();
		System.out.println(jobj);

		/*
		 * expected Output {"name":"Falco","age":3,"biteable":false}
		 * {"name":"Falco","age":3,"bitable":false}
		 */
	}

	public void parseOne() {
		// Stream parser, recommended for large json
		final String result = "{\"name\":\"Falco\",\"age\":3,\"bitable\":false}";
		final JsonParser parser = Json.createParser(new StringReader(result));
		String key = null;
		String value = null;
		while (parser.hasNext()) {
			final Event event = parser.next();
			switch (event) {
			case KEY_NAME:
				key = parser.getString();
				System.out.println(key);
				break;
			case VALUE_STRING:
				String string = parser.getString();
				System.out.println(string);
				break;
			case VALUE_NUMBER:
				BigDecimal number = parser.getBigDecimal();
				System.out.println(number);
				break;
			case VALUE_TRUE:
				System.out.println(true);
				break;
			case VALUE_FALSE:
				System.out.println(false);
				break;
			default:
				System.out.println("unknown event:" + event.toString());
				break;
			}
		} //end while
		parser.close();
		/* expected Output
		name
		Falco
		age
		3
		bitable
		false
		*/
	}
	
	public void doit (String json) {

	}

}
