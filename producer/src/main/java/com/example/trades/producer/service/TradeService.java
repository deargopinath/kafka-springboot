package com.example.trades.producer.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Srinivas Gopinath Parimi
 */
@Service
public class TradeService {
    private final Logger LOG = LoggerFactory.getLogger(TradeService.class);
    private List<String> trades = new ArrayList<>();
    
    private void setTrades() {
        if (!trades.isEmpty()) {
            return;
        }
        try (Stream<String> lines = Files.lines(Paths.get("C:/code/kafka-demo/producer/trades.csv"))) {
            trades = lines.collect(Collectors.toList());
            LOG.info("Found " + (trades.size() - 1) + "trades");
        } catch (IOException ex) {
            trades.add("Trades data is not available");
            LOG.error(ex.toString());
        }
    }
    
    public List<String> getTrades() {
        setTrades();
        return trades;
    }
    
    public String getRandomTrade() {
        setTrades();
        if (trades.size() < 2) {
            return "Trade data is not available";
        }
        Random r = new Random();
        int tradeID = r.nextInt(trades.size() - 1);
        if (tradeID == 0) {
            tradeID = trades.size() -1;
        }
        LOG.info("Trade ID = " + tradeID);
        return trades.get(tradeID);
    }
}
