package com.kyron.kafka;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kyron.util.MyFileUtils;

/**
 * How to use kafka stream
 * @author anh
 *
 */
public class MainStream {

	final static Logger LOG = LogManager.getLogger(MainStream.class.getName());
	static MyFileUtils util = new MyFileUtils();
		
	public static void main(String[] args) {
		// Read resource properties
		Properties props = util.readResourceProps("demo.properties");
		String inputTopic = props.getProperty("TOPIC_DEMO");
		String outputTopic = props.getProperty("TOPIC_OUT");
		System.out.println("in=" + inputTopic + ",out=" + outputTopic);

		runStream(inputTopic, outputTopic);
		
		System.out.println("done.");

	}
	
	static void runStream(String inputTopic, String outputTopic) {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "main-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        
        // create a stream from the input topic
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream(inputTopic);
        
        // can you do this?
        //stream.to(outputTopic);
        
        stream.mapValues(v -> v.toUpperCase())
        .peek((k,v) -> System.out.println("key="+k+", value="+v))
        .to(outputTopic);
        
        
	}

}
