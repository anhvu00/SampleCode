package com.goldsentinel; 


import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * Base class to test serialization and de-serialization of the POJOs.
 *
 * @param <T> POJO to test JSON serialization.
 *
 * @author neon
 * @since 0.10
 */
@NonNullByDefault
public abstract class BaseJsonSerializationTest<T> {

    /**Logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /** Default Jackson ObjectMapper to write/read to/from JSON to validate marshaling/unmarshaling. */
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

    /** Jackson ObjectMapper to convert to/from JSON. */
    private final ObjectMapper objectMapper;

    /* Class of tested POJO. */
    private final Class<T> testObjectClass;
    
    // board.json
    private T testBoard;

    /**
     * Constructs base test class instance using the default ObjectMapper.
     *
     * @param testObjectClass Class for test object. Cannot be null.
     */
    protected BaseJsonSerializationTest(Class<T> testObjectClass) {
        this(testObjectClass, DEFAULT_OBJECT_MAPPER);
    }

    /**
     * Constructs base test class instance.
     *
     * @param testObjectClass Class for test object. Cannot be null.
     * @param objectMapper ObjectMapper to use. Cannot be null.
     */
    protected BaseJsonSerializationTest(@NonNull Class<T> testObjectClass, @NonNull ObjectMapper objectMapper) {
//        this.testObjectClass = requireNonNull(testObjectClass, "testObjectClass cannot be null");
//        this.objectMapper = NonNull(objectMapper, "objectMapper cannot be null");
        this.testObjectClass = testObjectClass;
        this.objectMapper = objectMapper;
    }

    /** @return POJO to use in {@link #testJsonSerialzation()}. Never null. */
    protected abstract T getJsonSerialzationObject();

    /**
     * Override to implement additional checks as part of {@link #testJsonSerialzation()}.
     *
     * @param json Serialized JSON content for {@link #getJsonSerialzationObject()}.
     * @param deserializedObject Deserialized object from JSON.
     */
    protected void testJsonSerialzationAdditionalChecks(String json, T deserializedObject) {
    	LOG.debug("*** here *** ");
    }
    

    /**
     * Test JSON file(s) used by {@link #testDeserializeFromFile()}.
     *
     * @return Filenames to read JSON from in test/sources under same package as test class.
     *         Empty list to skip test case. Never null and items are never null.
     */
    protected abstract List<String> getDeserializeJsonFilenames();

    /** @return ObjectMapper used for test cases. Never null. */
    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Tests the serialization and deserialization processes.
     *
     * @throws JsonProcessingException Should not happen.
     */
    @Test
    public void testJsonSerialzation() throws JsonProcessingException {
        T object = getJsonSerialzationObject();
        LOG.debug("Serializing Object: {}", object);

        String json = objectMapper.writeValueAsString(object);
        LOG.debug("{} object to JSON:\n{}", testObjectClass.getSimpleName(), json);

        try {
            T deserializedObject = objectMapper.readValue(json, testObjectClass);
            LOG.debug("Deserialized Object: {}", deserializedObject);
            
            // save to class var for returning to caller
            setTestBoard(deserializedObject);

            testJsonSerialzationAdditionalChecks(json, deserializedObject);
        }
        catch (IOException e) {
            LOG.error("Unable to deserialize JSON to " + testObjectClass, e);
            fail("Unable to deserialize JSON to " + testObjectClass +  " - " + e);
        }
    }

    /**
     * Override to implement additional checks as part of {@link #testJsonSerialzation()}.
     *
     * @param deserializedObject Deserialized object from JSON.
     * @param jsonFile JSON filename for this test.
     */
    protected void testDeserializeFromFileAdditionalChecks(T deserializedObject, String jsonFile) {
    }

    /**
     * Tests the deserialization from a JSON file.
     *
     * @throws URISyntaxException Should not happen.
     * @throws IOException Should not happen.
     */
    @Test
    public void testDeserializeFromFile() throws URISyntaxException, IOException {
        List<String> jsonFiles = getDeserializeJsonFilenames();
        if (jsonFiles.isEmpty()) {
            LOG.info("Skipping test b/c no JSON file specified.");
            return;
        }

        for (String jsonFile : jsonFiles) {
            //Path resPath = Paths.get(getClass().getResource(jsonFile).toURI());
    		Path resPath = Paths.get("src", "test", "resources", jsonFile);
            String json = new String(Files.readAllBytes(resPath), StandardCharsets.UTF_8.name());
            LOG.debug("JSON from file [{}]:\n{}", jsonFile, json);

            T deserializedObject = objectMapper.readValue(json, testObjectClass);
            LOG.debug("Deserialized Object from file [{}]: {}", jsonFile, deserializedObject);

            testDeserializeFromFileAdditionalChecks(deserializedObject, jsonFile);
        }
    }
    
    //my test
    public T getTestBoard() {
    	return testBoard;
    }

	public void setTestBoard(T testBoard) {
		this.testBoard = testBoard;
	}

}
