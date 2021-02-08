package com.kyron.kafka;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.kyron.kafka.dto.JsonMessage;
import com.kyron.kafka.dto.KafkaJsonDeserializer;
import com.kyron.solr.SolrUtils;

/*
 * Intend to be a utility class with all necessary Kafka functions
 */
public class KafkaUtils<T> {
	
	final Class<T> DATA_TYPE;
	final static Logger LOG = LogManager.getLogger(KafkaUtils.class.getName());

	private Properties serverProp;
	private String serverPropFileName;
	
	// constructors
	public KafkaUtils(Class<T> dataType, String propFileName) {
		// Important must have.
		this.DATA_TYPE = dataType;
		// read properties
		serverProp = readProperties(propFileName);
		// keep for creating SolrUtils later
		serverPropFileName = propFileName;
	}
	
	/*
	 * insert a DTO to Kafka topic as json
	 * NOTE:
	 * - Move constants to server.properties file
	 * - What to do with "MSGKEY"?
	 * - Avoid creating a Producer each insert, performance?
	 */
	public<T> void insertJson(T object, String topic) {
		
		// Set properties for our Producer
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", serverProp.getProperty("kafka.broker"));
		props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.setProperty("value.serializer", "com.kyron.kafka.dto.KafkaJsonSerializer");
		Producer<String, T> producer = new KafkaProducer<>(props); 

		// Send a message with the POJO/DTO object (not json)
		producer.send(new ProducerRecord<>(topic, "MSGKEY", object));
		producer.close();
		System.out.println("message sent to topic " + topic);
	}
	
	/*
	 * Insert a string (json as string) to topic
	 */
	public<T> void insertString(T object, String topic) {
		
		// Set properties for our Producer
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", serverProp.getProperty("kafka.broker"));
		props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, T> producer = new KafkaProducer<>(props); 

		// Send a message with the POJO/DTO object (not json)
		producer.send(new ProducerRecord<>(topic, "MSGKEY", object));
		producer.close();
		System.out.println("message sent to topic " + topic);
	}
	
	/* 
	 * Consum json from kafka topic and insert to Solr
	 */
	public void kafkaJsonToSolr(String topic) {
		
		// Set properties for our Consumer
		Properties props = new Properties();
		props.setProperty("group.id", "test-consume");
		props.setProperty("bootstrap.servers", serverProp.getProperty("kafka.broker"));
//		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		props.setProperty("value.deserializer", "com.kyron.kafka.dto.KafkaJsonDeserializer");

		// T is JsonMessage, DATA_TYPE is JsonMessage.class = purely syntax
		Consumer<String, T> consumer = new KafkaConsumer(
				props,
				new StringDeserializer(),
				new KafkaJsonDeserializer(DATA_TYPE));
		consumer.subscribe(Collections.singletonList(topic));
		try {
			// Collect all new messages every 1 second
			ConsumerRecords<String, T> messages = consumer.poll(Duration.ofSeconds(1));
			if (messages != null && messages.count() > 0) {
				// Create solr util for this DTO
				SolrUtils su = new SolrUtils(DATA_TYPE, this.serverPropFileName);
				for (ConsumerRecord<String, T> message : messages) {
					// message.value() is JsonMessage type, send it to solr
					T msg = message.value();
					System.out.println("Message=" + msg.toString());
					su.insert(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		consumer.close();		
	}
	
	/* 
	 * Consume String (json) from kafka topic and insert to Solr
	 */
	public void kafkaStringToSolr(String topic) {
		
		// Set properties for our Consumer
		Properties props = new Properties();
		props.setProperty("group.id", "test-consume");
		props.setProperty("bootstrap.servers", serverProp.getProperty("kafka.broker"));
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		// T is JsonMessage, DATA_TYPE is JsonMessage.class = purely syntax
		Consumer<String, T> consumer = new KafkaConsumer(props);
		consumer.subscribe(Collections.singletonList(topic));
		try {
			// Collect all new messages every 1 second
			ConsumerRecords<String, T> messages = consumer.poll(Duration.ofSeconds(1));
			if (messages != null && messages.count() > 0) {
				// Create solr util for this DTO
				SolrUtils su = new SolrUtils(DATA_TYPE, this.serverPropFileName);
				for (ConsumerRecord<String, T> message : messages) {
					// this is how venom convert message to jsonobject
					JSONParser parser = new JSONParser();
					Object obj = parser.parse((String) message.value());
					if (obj instanceof JSONArray) {
						System.out.println("array json is not implemented yet");
					} else {
						JSONObject jsonObj = (JSONObject) obj;
						String item = (String) jsonObj.get("item");
						double price = (double) jsonObj.get("price");
						boolean avail = (boolean) jsonObj.get("available");
						// create a new DTO with this json object
						// NOTE: THIS DEFEATS THE GENERIC PURPOSE OF THIS UTIL
						JsonMessage dto = new JsonMessage(item, price, avail);
						su.insert(dto);
						System.out.println("inserted to solr");
					}
					
					// message.value() is JsonMessage type, send it to solr					
//					T msg = message.value();
//					System.out.println("Message=" + msg.toString());
//					su.insert(msg);
					
					/* example 4 - ran but no record
					final ContentStreamUpdateRequest csr = new ContentStreamUpdateRequest(su.getSolrUrl());
					final ContentStream cs = new ContentStreamBase.StringStream((String) msg);
					csr.addContentStream(cs);
					System.out.println("send string to solr");
					*/
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		consumer.close();		
	}
	
	// -------------------------
	// helper functions
	
	public Properties readProperties(String propFileName) {
		Properties retval = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			retval.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}

}
