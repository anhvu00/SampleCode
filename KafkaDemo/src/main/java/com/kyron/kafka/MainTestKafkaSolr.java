package com.kyron.kafka;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyron.kafka.dto.JsonMessage;
import com.kyron.kafka.dto.KafkaJsonDeserializer;
import com.kyron.kafka.dto.KafkaJsonSerializer;
import com.kyron.solr.SolrUtils;

/*
 * 11/4/20 
 * DONE:
 * - Kafka topic to Solr
 * TODO:
 * - Organize/abstract all params/constants (i.e. properties file name)
 * - Create KafkaUtils (stupid venomCacheData extends DBTable = too complicate to demo)
 */
public class MainTestKafkaSolr {

	public static void main(String[] args) {

		String topic = "demo";
//		testKafkaJsonToSolr(topic);
		testKafkaStringToSolr(topic);
	}
	
	/*
	 * Create a DTO with data
	 * Use Kafka Producer to insert the DTO to topic as Json
	 * Use Kafka Consumer to read Json into a DTO
	 * Use SolrUtil to save the DTO to Solr
	 */
	public static void testKafkaJsonToSolr(String topic) {
		// create a DTO
		JsonMessage jmsg = new JsonMessage("properties3", 3.99, true);
		KafkaUtils ku = new KafkaUtils(JsonMessage.class, "demo.properties");
		ku.insertJson(jmsg, topic);
		// save to solr
		ku.kafkaJsonToSolr(topic);	
	}
	
	/*
	 * Create a Json string
	 * Use Kafka Producer to insert the string to topic
	 * Use Kafka Consumer to read the string
	 * Use SolrUtil to save the string to Solr
	 */
	public static void testKafkaStringToSolr(String topic) {
		// Create a Json String
		JsonMessage jmsg = new JsonMessage("properties4", 4.99, true);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonString = mapper.writeValueAsString(jmsg);
			KafkaUtils ku = new KafkaUtils(String.class, "demo.properties");
			// insert string to kafka topic
			ku.insertString(jsonString, topic);
			// read and save string to solr
			ku.kafkaStringToSolr(topic);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}


	/*
	 * Create a DTO, Create a Producer with a Json Serializer specifically built for
	 * that DTO, Use the Producer to send in the DTO. The message key is hard-coded
	 * here.
	 */
	public static void insertJsonToKafka(String topic) {
		// create a DTO
		JsonMessage jmsg = new JsonMessage("burger", 5.85, true);

		// create a producer
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// Send obj using KafkaJsonSerializer
		props.put("value.serializer", "com.kyron.kafka.dto.KafkaJsonSerializer");

		Producer<String, JsonMessage> producer = new KafkaProducer<>(props, new StringSerializer(),
				new KafkaJsonSerializer());

		// Send a message with the POJO/DTO object (not json)
		producer.send(new ProducerRecord<>(topic, "MSGKEY", jmsg));
		producer.close();
		System.out.println("message sent to topic " + topic);
	}

	/*
	 * Requires topic, max number of message to pull per time interval (i.e. 1
	 * second) Create a Consumer with a Json Deserializer specifically built for the
	 * message DTO Use the Consumer to subscribe to the topic, pulling every second
	 * for maxNumMsgs Loop through the messages and print out
	 */
	public static void readKafka(String topic, int maxNumMsgs) {
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("group.id", "test-consume");

		Consumer<String, JsonMessage> consumer = new KafkaConsumer(props, new StringDeserializer(),
				new KafkaJsonDeserializer<JsonMessage>(JsonMessage.class));
		// Subscribe to the topic
		consumer.subscribe(Collections.singletonList(topic));
		try {
			int msgCnt = 0;
			while (true) {
				// collect all new messages every 1 second
				ConsumerRecords<String, JsonMessage> messages = consumer.poll(Duration.ofSeconds(1));
				// if no new message, try again n-times?
				if (messages.count() == 0) {
					msgCnt++;
					if (msgCnt > maxNumMsgs)
						break;
					else
						continue;
				}
				// if we have messages, process them
				for (ConsumerRecord<String, JsonMessage> message : messages) {
					if (messages == null) {
						System.out.println("null message");
					} else {
						System.out.println("Message=" + message.value().toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		consumer.close();
	}

	/*
	 * Same as readKafka() but simplified logic
	 */
	public static void kafkaToSolr(String topic) {
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("group.id", "test-consume");

		Consumer<String, JsonMessage> consumer = new KafkaConsumer(props, new StringDeserializer(),
				new KafkaJsonDeserializer<JsonMessage>(JsonMessage.class));
		// Subscribe to the topic
		consumer.subscribe(Collections.singletonList(topic));
		try {
			int msgCnt = 0;
			// collect all new messages every 1 second
			ConsumerRecords<String, JsonMessage> messages = consumer.poll(Duration.ofSeconds(1));
			if (messages != null && messages.count() > 0) {
				// Create solr util
				SolrUtils su = new SolrUtils(JsonMessage.class, "demo.properties");
				for (ConsumerRecord<String, JsonMessage> message : messages) {
					// message.value() is JsonMessage type, send it to solr
					JsonMessage msg = message.value();
					System.out.println("Message=" + msg.toString());
					// it cannot be simpler than this
					su.insert(msg);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		consumer.close();
	}
	

}
