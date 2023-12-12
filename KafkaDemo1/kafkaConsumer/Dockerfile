FROM openjdk:17-oracle
EXPOSE 8088
ADD target/kafkaConsumer-0.0.1-SNAPSHOT.jar kafkaConsumer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "kafkaConsumer-0.0.1-SNAPSHOT.jar"]
