# Java-Chat-Application-
Simple Java Chat Application with Socket communication

# Technology
The spring-boot-starter-web Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.
The project uses Websocket communication protocol with STOMP being the sub protocol.
The project use Spring Data JPA  for Persist data in SQL stores with Java Persistence API using Spring Data and Hibernate.
Lombok  for Java annotation library which helps to reduce boilerplate code.
H2 Database Provides a fast in-memory database that supports JDBC API and R2DBC access, with a small (2mb) footprint. Supports embedded and server modes as well as a browser based console application
Spring Boot Actuator Supports built in endpoints that let you monitor and manage your application - such as application health, metrics, sessions, etc.

#Features Support
- [X] Authorize user with basic authentication
- [X] Rest end point for th send and delete mesage 
- [X] Sending and receiving messages in the chat room.
- [X] Persists the messages in database table
- [X] Deletion of messages by clients.
- [X] get chat history of chat
- [X] Websocket chat Supported with public subisbiction topic.
- [X] Github workflow addrd for CI/CD

#Setting up
- Clone the project
- Open it on IDE and build the project using maven
- configuared with h2 database no need to change configration
##Running as docker
-  run `docker build --tag=java-chat:latest .`  
-  then run  `docker run -p8080:8080 java-chat:latest`

#$Running as typical Application
- Directly run the `ChatApplication.java` or run `mvn spring-boot:run` command 


#PostMan collection are available in Postman workspace

	##Users are hardcoded details given below
 	user1:user1
  	user2:user2

	Websocket
		https://www.postman.com/mutivenodrapp/workspace/chatapplication/collection/66090ea384b58c997bc16a12?action=share&creator=27990434

  
	Rest API
		https://www.postman.com/mutivenodrapp/workspace/chatapplication/collection/27990434-7ab09fca-9cde-4d37-9dcd-7e159bcbdd9f?action=share&creator=27990434
Fork the post man collection from my workspace in postman desktop
After running the application check the health api 
then user the rest or socket api






