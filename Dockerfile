# Stage 1: Build the app
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime container
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/app.jar /app/app.jar
EXPOSE 8089
CMD ["java", "-jar", "/app/app.jar"]
