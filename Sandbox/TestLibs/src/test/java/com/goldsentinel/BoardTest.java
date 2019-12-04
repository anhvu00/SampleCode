package com.goldsentinel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


//import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Tests the serialization and de-serialization of the {@link Board}.
 *
 * @author neon
 * @since 0.8
 */
@NonNullByDefault
public class BoardTest extends BaseJsonSerializationTest<Board> {

    /**Logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /** Jackson ObjectMapper to write/read to/from JSON to validate marshaling/unmarshaling. */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        // Be sure to bring in the JavaTimeModule for ZonedDateTime serialization.
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        // ParameterNamesModule is no longer needed with empty Builder constructor and with* methods
//      OBJECT_MAPPER.registerModule(new ParameterNamesModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    // test constants
    private static final ObjectNode DATA = OBJECT_MAPPER.createObjectNode();
    static {
        DATA.put("intField", 123);
        DATA.put("stringField", "test");
    }

    private static final String OWNER = "testuser";
    private static final Date NOW = new Date();

    private static final Project PROJECT = new Project.Builder(
            UUID.randomUUID(),
            "Test Project name",
            "Test Project description")
            .withCreatedBy(OWNER)
            .withCreatedOn(NOW)
            .withUpdatedBy(OWNER)
            .withUpdatedOn(NOW)
            .build();

    /** Test Board. */
    private static final Board BOARD = new Board.Builder(
            UUID.randomUUID(),
            "Test Board",
            DATA,
            OWNER,
            PROJECT)
            .withCreatedBy(OWNER)
            .withCreatedOn(NOW)
            .withUpdatedBy(OWNER)
            .withUpdatedOn(NOW)
            .build();

    /**
     * Constructs test case.
     */
    public BoardTest() {
        super(Board.class, OBJECT_MAPPER);
    }

    @Override
    protected Board getJsonSerialzationObject() {
    	// this is the static board, how to get the board.json?
        return BOARD;
    }

    @Override
    protected List<String> getDeserializeJsonFilenames() {

        return Arrays.asList("board.json");
    	
    }
    
    // my test
    @Override
	protected void testJsonSerialzationAdditionalChecks(String json, Board deserializedObject) {
    	LOG.info("*** String = " + json);
    	LOG.info("*** Board = " + deserializedObject);    	
    }
}
