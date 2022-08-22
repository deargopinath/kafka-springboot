package com.example.trades.producer.controller;

import com.example.trades.producer.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class TradeController {
    
    private final Logger LOG = LoggerFactory.getLogger(TradeController.class);
    
    // Autowiring Kafka Template
    @Autowired KafkaTemplate<String, String> kafkaTemplate;
 
    @Autowired
    TradeService tradeService;
    
    private static final String TOPIC = "trades";
 
    // Publish messages using the GetMapping
    @GetMapping("/publish/{message}")
    public String publishMessage(@PathVariable("message")
                                 final String message) {
        String trade = tradeService.getRandomTrade();
        kafkaTemplate.send(TOPIC, trade);
        LOG.info("trade sent = " + trade.replaceAll(",", "\t").trim());
        return trade;
    }
}
