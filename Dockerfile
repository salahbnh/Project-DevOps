# Start with a base image containing Java runtime
FROM maven:3.8.4-openjdk-8-slim as builder

WORKDIR /app
# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Start with a minimal base image for running the app
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY --from=builder /app/target/gestion-station-ski-1.0-SNAPSHOT.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/app.jar"]
