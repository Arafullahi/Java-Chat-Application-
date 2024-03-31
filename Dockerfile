
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package



FROM openjdk:21-jdk
COPY --from=build /home/app/target/chat-0.0.1-SNAPSHOT.jar chat-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/chat-0.0.1.jar"]
