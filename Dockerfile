# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine
VOLUME /tmp
# Copy the jar to the container
COPY target/gestion-station-ski-1.0-SNAPSHOT.jar app.jar
# Expose the application port
EXPOSE 8089
# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]
