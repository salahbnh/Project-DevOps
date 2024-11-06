# Use Maven image for building and testing
FROM maven:3.8.5-openjdk-17 as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project into the container (including pom.xml)
COPY . .

# Run the build (optional)
RUN mvn clean install -DskipTests

# Use a slim JDK image for the runtime container
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]