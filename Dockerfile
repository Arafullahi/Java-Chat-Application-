FROM openjdk:21-jdk
COPY target/chat-0.0.1-SNAPSHOT.jar chat-0.0.1.jar
ENTRYPOINT ["java","-jar","/chat-0.0.1.jar"]