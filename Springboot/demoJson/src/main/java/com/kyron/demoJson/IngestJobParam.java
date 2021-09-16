package com.kyron.demoJson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An enumeration for job parameters and their defaults.
 */
@Getter
public enum IngestJobParam {
	SEARCH_DATE("searchDate", ""),
	PUBDATE_SELECTOR("pubDate",	"pubDate"),
	LINK_SELECTOR("link", "link"),
	DESCRIPTION_SELECTOR("description",	"description"),
	PUBDATE_FORMAT("pubDateFormat",	"EEE, dd MMM yyyy HH:mm zzz"),
	ITEM_SELECTOR("item", "item"),
	TITLE_SELECTOR("title", "title"),
	CONTENT_SELECTOR("content",	"*"),
	COOKIES("cookies"),
	HEADERS("headers"),
	SECONDARY_LINK("secondaryLink"),
	FILTER_BY_PUBDATE("filterByPubDate", "false"),
	COLUMNS_TO_PUBLISH("columnsToPublish"),
	QUEUE_NAME("queueName", "INGEST"),
	QUEUE_TYPE("queueType","NONE"),
	PRIMARY_KEY("primaryKey"),
	CLASSIFICATION("classification"),
	DATASOURCE_ID("dataSourceId"),
	BOILERPIPE_EXTRACTOR("boilerpipeExtractor", "KEEP_EVERYTHING_EXTRACTOR"),
	PROCESSING_PATH("processingPath", "default"),
	XSL_TRANSFORMATION("xslTransformation", "default"),
	SUPPLEMENTAL_DATA_COLUMNS("supplementalDataColumns"),
	USERNAME("username"),
	TOKEN("token"),
	PRIV_KEY("privateKey"),
	PUB_KEY("publicKey"),
	FILENAME_REGEX("filenameRegex", ".*"),
	FILENAME_REGEX_OPTION("filenameRegexOption", "INCLUDE"),
	CONTENT_REGEX("contentRegex", ".*"),
	CONTENT_REGEX_OPTION("contentRegexOption", "INCLUDE"),
	USE_ALT_FILENAME("useAltFilename", "false"),
	FILE_EXT_OVERRIDE("fileExtensionOverride"),
	SERVER_RETRIEVE_TYPE("serverRetrieveType","GET"),
	RECURSIVE("recursive", "false", "A toggle for recursively ingesting paginated or nested data sources"),
	MAX_DEPTH("maxDepth", "1", "The maximum data source depth to recursively ingest (0-based)"),
	CUR_DEPTH("currentDepth", "0", "The starting or current data source page/level being ingested (0-based)"),
	REMOTE_FOLDER("remoteFolder", "/"),
	THRESHOLD("threshold", "10"),
	HOST("host", ""),
	PORT("port", ""),
	EMAIL_RETRIEVE_OPTION("emailRetrieveOption", "RetrieveAll"),
	FILENAME_FIELD("filenameField"),
	PARSE_STRATEGY("parseStrategy"),
	FETCH_STRATEGY("fetchStrategy"),
	PUBLISH_CONTENT_STRATEGY("publishContentStrategy"),
	CLEANUP_STRATEGY("cleanupStrategy");

	/** The parameter key that is used to lookup its value. */
	private final String key;

	/** The default value to use for this parameter if it is not configured */
	private final String defaultValue;

	/** The description for this job param */
	private final String description;

	/**
	 * Convenience constructor.
	 *
	 * @param key the job param key
	 */
	IngestJobParam(String key) {
		this(key, null, null);
	}

	/**
	 * Convenience constructor.
	 *
	 * @param key the job param key
	 * @param defaultValue the job param default value
	 */
	IngestJobParam(String key, final String defaultValue) {
		this(key, defaultValue, null);
	}

	/**
	 * All args constructor.
	 *
	 * @param key the job param key
	 * @param defaultValue the job param default value
	 */
	IngestJobParam(@NonNull final String key, final String defaultValue, final String description) {
		if (key.isEmpty() || !Character.isLowerCase(key.charAt(0))) {
			throw new IllegalArgumentException(String.format("Job parameter key must not be empty and must be in camel case: {%s}", key));
		}

		this.key = key;
		this.defaultValue = defaultValue;
		this.description = description;
	}

	/**
	 * Returns this {@link IngestJobParam} key/value pair as a singleton map consisting
	 * of the key and the default value.
	 *
	 * @return map of this job param key and default value
	 */
	@JsonValue
	public Map<String, String> getAsMap() {
		Map<String, String>map = new LinkedHashMap<>();
		map.put("key", key);
		if (hasDefaultValue()) {
			map.put("default", defaultValue);
		}
		return map;
	}
		
	/**
	 * Returns true if this {@link IngestJobParam} has a default value specified.
	 *
	 * @return true if has default value
	 */
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}
	
    /**
     * Returns the correct enum matching the supplied key string.
     *
     * @param key the param key
     * @return the matching enum
     */
    @JsonCreator
    public static IngestJobParam fromString(String key) {
        return key == null
                ? null
                : IngestJobParam.valueOf(key.toUpperCase());
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return key;
	}

	/**
	 * Returns the parameter value from the provided {@link Map}. If the parameter is not found in
	 * the map, the parameter's default will be returned.
	 *
	 * @param params the param source
	 * @return the param value
	 */
	public String getOrDefaultFrom(final Map<String, String> params) {
		return params.getOrDefault(key, defaultValue);
	}

	/**
	 * Returns the parameter value from the provided {@link Map}. If the parameter is not found in
	 * the map, a null value will be returned.
	 * 
	 * @param params the param source
	 * @return the param value
	 */
	public String getFrom(final Map<String, String> params) {
		return params.get(key);
	}

	/**
	 * Returns true if this parameters exists in the provided {@link Map}.
	 * 
	 * @param params the param source
	 * @return true if map contains parameter
	 */
	public boolean existsIn(final Map<String, String> params) {
		return params.containsKey(key);
	}

	/**
	 * Puts this parameter in the provided map with the provided value and returns the replaced
	 * value, if one existed.
	 *
	 * @param params the param source
	 * @param value the param value
	 */
	public void putIn(final Map<String, String> params, String value) {
		params.put(key, value);
	}

	/**
	 * Removes this parameter from the provided map.
	 *
	 * @param params the param source
	 */
	public void removeFrom(final Map<String, String> params) {
		params.remove(key);
	}

}
