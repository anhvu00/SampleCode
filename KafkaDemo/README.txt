10/12/20
what we could program with Kafka producer, consumer, stream (KStream, KTable)
make a utility library

Need:
- Start Zookeeper service (localhost)
- Start Kafka server (localhost)

DONE:
Java DTO/POJO -> Kafka producer -> Kafka topic
Kalfka topic -> Kafka consumer -> deserialize to java DTO/POJO
TODO:
Kalfka topic -> Kafka stream enrich -> another Kafka topic
java DTO/POJO -> Solr Document -> Solr (via http. Need a running local Solr server)

Input sample data to zoo

cd D:\Software\Kafka\26\bin\windows

[start zoo on windows]
zookeeper-server-start.bat ..\..\config\zookeeper.properties

[start kafka on windows]
kafka-server-start.bat ..\..\config\server.properties

[create a kafka topic "streams-plaintext-input"]
kafka-topics.bat --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic streams-plaintext-input
kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic streams-plaintext-input
[create a kafka topic "streams-wordcount-output"]
kafka-topics.bat --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic streams-wordcount-output \
    --config cleanup.policy=compact

[describe topics]
kafka-topics.bat --bootstrap-server localhost:9092 --describe

[write events to topic]
kafka-console-producer.bat --bootstrap-server localhost:9092 --topic streams-plaintext-input
this is my event 1
this is my event 2
<ctrl-c>

[read events from another terminal]
kafka-console-consumer.bat --from-beginning --bootstrap-server localhost:9092 --topic streams-plaintext-input

[note: topic name is easily mispelled]
[delete a topic]
kafka-topics.bat --bootstrap-server localhost:9092 --delete --topic streams-plaintex-input

[in eclipse or command line kafka-run, run the wordcount java main, then you can see the output as followed]

[format output]
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic streams-wordcount-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic streams-wordcount-output --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer  --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

[connector (file-to-file)]
connect-standalone.bat ..\..\config\connect-standalone.properties ..\..\config\connect-file-source.properties ..\..\config\connect-file-sink.properties

[connector (file-to-solr)]