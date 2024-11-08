# Use Maven image with Java 17
FROM maven:3.8.1-openjdk-17-slim

# Copy the source code and dependencies to the container
COPY . /app
WORKDIR /app

# Build the application inside the container
RUN mvn clean package -DskipTests

# Expose port 8089
EXPOSE 8089

# Start the app
ENTRYPOINT ["java", "-jar", "target/gestion-station-ski-1.0.jar"]
