package com.kyron.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyron.kafka.dto.JsonMessage;
import com.kyron.kafka.dto.KafkaJsonDeserializer;
import com.kyron.kafka.dto.KafkaJsonSerializer;
import com.kyron.kafka.dto.User;
import com.kyron.util.MyFileUtils;

/*
 * To run this app, you need:
 * - Run Zookeeper localhost
 * - Run Kafka localhost
 * - Create topic "demo" and "user"
 */
public class MainApp {
	final public static String PLAINTEXT_TOPIC = "streams-plaintext-input";
	final public static String PIPE_OUTPUT_TOPIC = "streams-pipe-output";

	final static Logger LOG = LogManager.getLogger(MainApp.class.getName());
	static MyFileUtils util = new MyFileUtils();

	public static void main(String[] args) {

		// Read resource properties
		Properties props = util.readResourceProps("demo.properties");

		// Send and receive message to/from topic "demo" with default String
		// de/serialization
		int maxNumMsgs = Integer.parseInt(props.getProperty("MAX_NO_MESSAGES"));
		String demoTopic = props.getProperty("TOPIC_DEMO").trim();
		runProducer(demoTopic, maxNumMsgs);
		runConsumer(demoTopic, maxNumMsgs);

		// Send and receive one message to/from topic "user" with customized
		// de/serialization
		String userTopic = props.getProperty("TOPIC_USER").trim();
		runProducerUserSerializer(userTopic);
		runConsumerUserDeserializer(userTopic, 1);

		// Send and receive json to/from topic "json-string" with customized
		// de/serialization
		String jsonTopic = props.getProperty("TOPIC_JSON").trim();
		runProducerJson(jsonTopic);
		runConsumerJson(jsonTopic, 1);

//		runStreamPipe();
		System.out.println("done");
	}

	/*
	 * Producer sends a Json string to topic "json-string" 2 ways to do this: 1.
	 * POJO/DTO --> JSON String --> Producer --> Topic 2. POJO/DTO --> Producer -->
	 * Topic This function implements the 2nd way.
	 */
	static void runProducerJson(String topic) {

		// create a DTO
		JsonMessage jmsg = new JsonMessage("coke", 2.45, true);

		// create a producer
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		// 1. The first way: write obj to string
		// props.put("value.serializer",
		// "org.apache.kafka.common.serialization.StringSerializer");
		// Producer<String, String> producer = new KafkaProducer<>(props);
		// JsonMessage jmsg = new JsonMessage("ketchup", 2.95, true);
		// ObjectMapper mapper = new ObjectMapper();
		// String jsonString = mapper.writeValueAsString(jmsg);

		// 2. The second way: send obj using KafkaJsonSerializer
		props.put("value.serializer", "com.kyron.kafka.dto.KafkaJsonSerializer");
		Producer<String, JsonMessage> producer = new KafkaProducer<>(props, new StringSerializer(),
				new KafkaJsonSerializer());
		// Send a message with the POJO/DTO object (not json)
		producer.send(new ProducerRecord<>(topic, "MSGKEY", jmsg));
		producer.close();
	}

	/*
	 * Consumer subscribed to topic "json-string" gets 1 message (json)
	 * and return a JsonMessage object (java POJO/DTO).
	 * It is automatic binding/mapping json --> java object
	 */
	static void runConsumerJson(String topic, int maxNumMsgs) {
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("group.id", "test-2");

		Consumer<String, JsonMessage> consumer = new KafkaConsumer(props, new StringDeserializer(),
				new KafkaJsonDeserializer<JsonMessage>(JsonMessage.class));
		// Subscribe to the topic
		consumer.subscribe(Collections.singletonList(topic));
		try {
			int msgCnt = 0;
			while (true) {
				ConsumerRecords<String, JsonMessage> messages = consumer.poll(Duration.ofSeconds(1));
				if (messages.count() == 0) {
					msgCnt++;
					if (msgCnt > maxNumMsgs)
						break;
					else
						continue;
				}
				for (ConsumerRecord<String, JsonMessage> message : messages) {
					if (messages == null) {
						LOG.info("null message");
					} else {
						LOG.info("Message=" + message.value().toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		consumer.close();
	}

	/*
	 * Simple producer writes 0-9 as keys and values to topic "demo"
	 */
	static void runProducer(String topic, int maxNumMsgs) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 10; i++)
			// send key and value pair to "demo"
			producer.send(
					new ProducerRecord<String, String>(topic, Integer.toString(i), "MESSAGE " + Integer.toString(i)));

		producer.close();
	}

	/*
	 * Simple consumer group.id "test" subsrcibes to topic "demo"
	 */
	static void runConsumer(String topic, int maxNumMsgs) {
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("group.id", "test");
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.commit.interval.ms", "1000");
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		// subsribe to a list of topics
		consumer.subscribe(Arrays.asList(topic));
		int msgCnt = 0;
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
			if (records.count() == 0) {
				msgCnt++;
				if (msgCnt > maxNumMsgs)
					break;
				else
					continue;
			}
			records.forEach(record -> {
				String msg = "offset=" + record.offset() + ",key=" + record.key() + ", value=" + record.value();
				LOG.info(msg);
			});
		}
		consumer.close();
	}

	/*
	 * Producer sends a User object to topic "user", with customized UserSerializer
	 */
	static void runProducerUserSerializer(String topic) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "com.kyron.kafka.dto.UserSerializer");

		Producer<String, User> producer = new KafkaProducer<>(props);

		// create a user
		User u = new User("anh", 27);

		try {
			producer.send(new ProducerRecord<String, User>(topic, u));
			LOG.info("Message " + u.toString() + " sent !!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		producer.close();
	}

	/*
	 * Consumer group.id "test-1" uses a customized UserDeserializer to print User
	 * object from topic "user". Recommend run only 1 (maxNumMsgs=1) because only 1
	 * message was sent.
	 */
	static void runConsumerUserDeserializer(String topic, int maxNumMsgs) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test-1");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "com.kyron.kafka.dto.UserDeserializer");

		KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props);
		try {
			consumer.subscribe(Collections.singletonList(topic));
			int msgCnt = 0;
			while (true) {
				ConsumerRecords<String, User> messages = consumer.poll(Duration.ofSeconds(1));
				if (messages.count() == 0) {
					msgCnt++;
					if (msgCnt > maxNumMsgs)
						break;
					else
						continue;
				}
				for (ConsumerRecord<String, User> message : messages) {
					if (messages == null) {
						LOG.info("null message");
					} else {
						LOG.info("Message=" + message.value().toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		consumer.close();
	}

// ---------------------------------------------

	/*
	 * ? this doesn't do anything. why?
	 */
	static void runStreamPipe() {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		final StreamsBuilder builder = new StreamsBuilder();
		KStream<String, String> source = builder.stream(PLAINTEXT_TOPIC);
		// source.to(OUTPUT_STREAM);
		source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
				.groupBy((key, value) -> value)
				.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store")).toStream()
				.to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
		final Topology topology = builder.build();
		final KafkaStreams streams = new KafkaStreams(topology, props);
		final CountDownLatch latch = new CountDownLatch(1);
		System.out.println(topology.describe());

		// attach shutdown handler to catch control-c
		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				streams.close();
				latch.countDown();
			}
		});

		try {
			streams.start();
			latch.await();
		} catch (Throwable e) {
			System.exit(1);
		}
		System.exit(0);
	}

	/*
	 * Various KStream examples. Want: Source=Kafka, Sink=Solr
	 */
	static void createKStream(String topic) {
		StreamsBuilder builder = new StreamsBuilder();

		KStream<String, Integer> wordCounts = builder.stream(topic, Consumed.with(Serdes.String(), Serdes.Integer()));

	}
}
