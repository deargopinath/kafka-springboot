package com.example.trades.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    KafkaReader reader;

    @MessageMapping("/query")
    @SendTo("/topic/trades")
    public WebSocketResponse trade(HelloMessage message) throws Exception {
        return new WebSocketResponse(reader.getLatestMessage());
    }
}
