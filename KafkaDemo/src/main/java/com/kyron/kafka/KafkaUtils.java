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
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kyron.kafka.dto.KafkaJsonDeserializer;
import com.kyron.kafka.dto.KafkaJsonSerializer;
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
	 * Consume json from kafka topic and insert to Solr
	 */
	public void kafkaToSolrJson(String topic) {
		
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
