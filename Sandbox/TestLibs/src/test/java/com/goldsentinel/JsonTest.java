package com.goldsentinel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.JsonPatchBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class JsonTest {
	// Logger for this class.

	private static final Logger LOG = LoggerFactory.getLogger(JsonTest.class);

	/**
	 * Default Jackson ObjectMapper to write/read to/from JSON to validate
	 * marshaling/unmarshaling.
	 */
	private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();
	static {
		// Be sure to bring in the JavaTimeModule for ZonedDateTime serialization.
		DEFAULT_OBJECT_MAPPER.registerModule(new JavaTimeModule());
		DEFAULT_OBJECT_MAPPER.registerModule(new ParameterNamesModule());
		DEFAULT_OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		DEFAULT_OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
		DEFAULT_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		DEFAULT_OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Test
	public void myJpatch() throws FileNotFoundException {
		LOG.debug("my json test starts...");
		JsonPatchBuilder jsonPatchBuilder = Json.createPatchBuilder();
		JsonPatch jsonPatch = jsonPatchBuilder.add("/sex", "M").remove("/age").build();

		String jsonFile = "person.json";
		Path resourcePath = Paths.get("src", "test", "resources", jsonFile);
		LOG.info("path=" + resourcePath.toString());
		JsonReader reader = Json.createReader(new FileReader(resourcePath.toString()));
		JsonStructure jsonStructure1 = reader.read();
		JsonStructure jsonStructure2 = jsonPatch.apply(jsonStructure1);
		System.out.println(jsonStructure2);
		reader.close();
		LOG.info("my json test ends...");
	}

	@Test
	public void givenJsonArray_whenDeserializingAsListWithTypeReferenceHelp_thenCorrect()
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		List<BoardNode> listOfDtos = new ArrayList<BoardNode>();
		listOfDtos.add(new BoardNode("type1", "a2838", 100, 200, "", true, ""));
		listOfDtos.add(new BoardNode("type2", "38746", 333, 222, "", false, ""));
		String jsonArray = mapper.writeValueAsString(listOfDtos);

		List<BoardNode> asList = mapper.readValue(jsonArray, new TypeReference<List<BoardNode>>() {
		});

		for (BoardNode b : asList) {
			LOG.info(b.toString());
		}
	}

	// this doesn't work as designed...
	// @Test
	public void myEdit() throws IOException {
		LOG.debug("--- my test Update starts...");

		String jsonFile = "board1.json";
		Path resourcePath = Paths.get("src", "test", "resources", jsonFile);

		JsonReader reader = Json.createReader(new FileReader(resourcePath.toString()));
		JsonStructure jsonStructure1 = reader.read();
		reader.close();
		navigateTree(jsonStructure1, null);
		// how can I use board.java here???
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(new File(resourcePath.toString()));
		JsonNode notes = root.get("notesNodes");
		ArrayNode aryNotes = (ArrayNode) root.get("notesNodes");

		TypeFactory typeFactory = mapper.getTypeFactory();
		String jsonString = notes.toString();
//		List<BoardNode> list = mapper.readValue(jsonString, new TypeReference<List<BoardNode>>(){});
//		List<BoardNode> someClassList = mapper.readValue(jsonString,
//				typeFactory.constructCollectionType(List.class, Board.class));
		List<BoardNode> list = mapper.reader().forType(new TypeReference<List<BoardNode>>() {
		}).readValue(jsonString);
		for (BoardNode b : list) {
			String msg = b.toString();
			LOG.info(msg);
		}
		LOG.info("--- my test Update ends...");
	}

	@Test
	public void mkyongTest() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonFile = "board1.json";
		Path resourcePath = Paths.get("src", "test", "resources", jsonFile);
		JsonNode root = mapper.readTree(new File(resourcePath.toString()));
		JsonNode notesAry = root.get("notesNodes");

		System.out.println("*** BEFORE ***");
		printJsonAry(notesAry);

		String targetId = "1566402360566-0.2146340420459225";
		for (JsonNode node : notesAry) {
			String id = node.path("id").textValue();
			if (targetId.equals(id)) {
				((ObjectNode) node).put("x", 99);
				((ObjectNode) node).put("y", 55);
			}
		}
		System.out.println("*** AFTER ***");
		printJsonAry(notesAry);

		// update root
		((ObjectNode) root).set("notesNodes", notesAry);
		notesAry = root.get("notesNodes");

		System.out.println("*** AFTER UPDATED ***");
		printJsonAry(notesAry);
	}
	
	// similar to mkyongTest but replace the ary[i] with the new data
	@Test
	public void searchAndReplaceNode() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		// read data node
		String nodeFile = "node.json";
		Path resourcePath = Paths.get("src", "test", "resources", nodeFile);
		JsonNode root = mapper.readTree(new File(resourcePath.toString()));

		System.out.println("*** DATA NODE ***");
		System.out.println(root.toString());
		
		// read target board
		String jsonFile = "board1.json";
		Path resourcePath1 = Paths.get("src", "test", "resources", jsonFile);
		JsonNode root1 = mapper.readTree(new File(resourcePath1.toString()));
		ArrayNode notesAry = (ArrayNode) root1.get("notesNodes");

		System.out.println("*** TARGET BOARD ***");
		printJsonAry1(notesAry);
		
		// search and replace
		for (int i=0; i < notesAry.size(); i++) {
			if (notesAry.get(i).get("id").asText().equalsIgnoreCase(root.get("id").asText())) {
				System.out.println("FOUND");
				notesAry.remove(i);
				notesAry.add(root);
				break;
			}
		}
		System.out.println("*** AFTER SEARCH AND REPLACE ***");
		printJsonAry1(notesAry);		
		
	}
	
	// helper
	public void printJsonAry1(JsonNode aryNode) {
		for (JsonNode node : aryNode) {
			System.out.println(node.toString());
		}
	}
	
	// helper
	public void printJsonAry(JsonNode aryNode) {
		for (JsonNode node : aryNode) {
			String id = node.path("id").textValue();
			int x = node.path("x").asInt();
			int y = node.path("y").asInt();
			System.out.println(id + "," + x + "," + y);
		}
	}

	public static void navigateTree(JsonValue tree, String key) {

		if (key != null)
			System.out.print("Key " + key + ": ");
		switch (tree.getValueType()) {
		case OBJECT:
			System.out.println("OBJECT");
			JsonObject object = (JsonObject) tree;
			for (String name : object.keySet())
				navigateTree(object.get(name), name);
			break;
		case ARRAY:
			System.out.println("ARRAY");
			JsonArray array = (JsonArray) tree;
			for (JsonValue val : array)
				navigateTree(val, null);
			break;
		case STRING:
			JsonString st = (JsonString) tree;
			System.out.println("STRING " + st.getString());
			break;
		case NUMBER:
			JsonNumber num = (JsonNumber) tree;
			System.out.println("NUMBER " + num.toString());
			break;
		case TRUE:
		case FALSE:
		case NULL:
			System.out.println(tree.getValueType().toString());
			break;
		}
	}

}
