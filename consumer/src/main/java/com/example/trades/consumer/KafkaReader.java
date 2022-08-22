package com.example.trades.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaReader {

    private String latestMessage = "";
    private String previousMessage = "duplicate-data";
    private static int counter = 0;
    private static final Logger LOGGER =
      LoggerFactory.getLogger(KafkaReader.class);

  @KafkaListener(topics = "${kafka.topic}")
  public void receive(String payload) {
    LOGGER.info("received payload='{}'", payload);
    System.out.println(payload);
    if(counter == 100) {
        latestMessage = payload;
        counter = 0;
    } else {
        counter++;
        latestMessage += (payload + "\n");
    }
    if(latestMessage.equalsIgnoreCase(previousMessage)) {
        latestMessage = "duplicate-data";
    } else {
        previousMessage = latestMessage;
    }
  }
  public String getLatestMessage() {
      return latestMessage.trim();
  }
}