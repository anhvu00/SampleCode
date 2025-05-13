# Use an official OpenJDK runtime as a base image
FROM openjdk:17-oracle
# Set the working directory inside the container
WORKDIR /app
COPY target/kafkaConsumer-0.0.1-SNAPSHOT.jar /app/
# Specify the command to run your application
ENTRYPOINT ["java", "-jar", "kafkaConsumer-0.0.1-SNAPSHOT.jar"]
# The application listens on 8088. Exposing it here doesn't automatically 
# publish it to the host machine (still need a service.yaml and expose it there). 
EXPOSE 8088
