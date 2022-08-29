# Kafka data streams - Demo

```
+---------------+
|    Browser    |
+-------+-------+
        | 
	^  Websocket
	|
+-------+-------+
|   Consumer    |
+-------+-------+
        |
	^  Connector
	| 
+-------+-------+
|  Kafka topic  |
+-------+-------+   
        |
	^  Connector
	| 
+-------+-------+
|   Producer    |
+---------------+

```

# Instructions to deploy and demonstrate
----------------------------------------

1. Install Kafka 
     - https://www.confluent.io/download  
     - https://kafka.apache.org/downloads

2. Start Kafka service.

3. Create kafka topic named 'trades'.

4. Build the producer and consumer projects using 'mvn clean install'.

5. Run `java -jar target\producer-1.0.0.jar to send data to kafka topic.
	- http://localhost:8888

6. Run java -jar target\consumer-1.0.0.jar to start consumer service.
	- http://localhost:8080


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#web)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#messaging.kafka)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
