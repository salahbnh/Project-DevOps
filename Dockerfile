# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy only the JAR file into the container
# Adjust the path to match your project's target directory in Jenkins
COPY target/*.jar app.jar

# Expose port 8080 to the outside world (or adjust as needed)
EXPOSE 8089

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
