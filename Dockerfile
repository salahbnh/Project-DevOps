FROM openjdk:8
EXPOSE 8089
ADD target/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]