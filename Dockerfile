# Stage 1: Build the app
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app

# Copy only the pom.xml file and download dependencies first to leverage Docker cache
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the rest of the project and build the application
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime container
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy the Spring Boot application JAR file from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Expose the port the app runs on
EXPOSE 8089

# Run the application
CMD ["java", "-jar", "/app/app.jar"]
