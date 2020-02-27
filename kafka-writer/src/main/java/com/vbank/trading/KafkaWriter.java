package com.vbank.trading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Component
@Slf4j
public class KafkaWriter implements CommandLineRunner {
    
    @Value("${kafka.topic}")
    private String kafkaTopic;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void run(String... args) throws Exception {
        publish(kafkaTopic, getTrades());
    }
    
    private void publish(String topic, List<String> messages) {
        AtomicLong onFailureCount = new AtomicLong(0);
	AtomicLong onSuccessCount = new AtomicLong(0);
	CountDownLatch countDownLatch = new CountDownLatch(messages.size());
        for(String message: messages) {
            ListenableFuture<SendResult<String, String>> sendResult = 
                    kafkaTemplate.send(topic, message);
            sendResult.addCallback(
                    new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> sendResult) {
                    onSuccessCount.incrementAndGet();
                    ProducerRecord<String, String> producerRecord = 
                        		sendResult.getProducerRecord();
                    RecordMetadata recordMetadata = sendResult.getRecordMetadata();
                    countDownLatch.countDown();
                    log.info("onSuccess(), offset {} partition {} timestamp {} for '{}'=='{}'",
                    		recordMetadata.offset(),
                    		recordMetadata.partition(), 
                                recordMetadata.timestamp(), 
                    		producerRecord.key(), 
                                producerRecord.value());
                }
                @Override
                public void onFailure(Throwable t) {
                    onFailureCount.incrementAndGet();
                    countDownLatch.countDown();
                    log.error("onFailure()", t);
                }
            }
            );
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException ex) {
            log.error(ex.toString());
        }
        if (onFailureCount.get() > 0) {
            throw new RuntimeException(onFailureCount.get() + " failures writing to Kafka");
        } else {
            log.info("Wrote {} message{}", onSuccessCount.get(), (onSuccessCount.get() == 1 ? "" : "s"));
	}
    }
    
    private List<String> getTrades() throws IOException, InterruptedException {
        List<String> result = new ArrayList<>();
        Resource resource = this.applicationContext
                .getResource("classpath:trades.txt");
        try(InputStream inputStream = resource.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
           ){
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if(line.contains("#")) {
                        continue;
                    }
                    String[] tokens = line.split(",");
                    // Symbol , Name , Price  , Change , Volume , Market_Cap , PE_Ratio
                    if(tokens.length < 7) {
                        continue;
                    }
                    LinkedHashMap<String, String> h = new LinkedHashMap<>();
                    h.put("SYMBOL", tokens[0].trim());
                    h.put("NAME", tokens[1].trim());
                    h.put("PRICE", tokens[2].trim());
                    h.put("CHANGE", tokens[3].trim());
                    h.put("VOLUME", tokens[4].trim());
                    h.put("MARKET_CAP", tokens[5].trim());
                    h.put("PE_RATIO", tokens[6].trim());
                    result.add(getJSON(h));
		}
        }
        return result;
    }
    private String getJSON(LinkedHashMap<String,String> hashMap) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	return gson.toJson(hashMap);
	//System.out.println(jsonString);
       /*         
          String json = ("{");
          json = hashMap.keySet().stream()
               .map((x) -> ('"' + x + '"' + ':' + '"' + hashMap.get(x) + '"' + ','))
               .reduce(json, String::concat);
          json += "}";
          return json.replaceAll(",}", "}");
       */ 
    }
}
