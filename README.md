# Stream kafka messages over web socket

```ignorelang

+-----------------------------+
|          Browser            |
+-------------+---------------+
              |
              ^
              |
+-------------+---------------+
|         Web Socket          |
+-------------+---------------+
              |   
              ^    
              |     
+-------------+---------------+
|         Kafka topic         |
+-----------------------------+

```

# Instructions to deploy and demonstrate

1. Install Kafka (https://www.confluent.io/download/) at C:\confluent41
2. Start Kafka service
3. Create kafka topic named 'trades'
4. Build the kafka-writer and kafka-websocket projects using mvn clean install
5. Run `java -jar target\kafka-writer.jar to send test data to kafka topic.
6. Run java -jar target\kafka-websocket.jar to start websocket service
7. Open http://localhost:8080 to see websocket data on browser
