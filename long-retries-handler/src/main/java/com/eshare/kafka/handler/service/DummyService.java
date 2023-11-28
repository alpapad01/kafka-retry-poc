package com.eshare.kafka.handler.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class DummyService {

    private final SendBatchService sendBatch;

    @PostConstruct
    public void doIt() {
        
        CompletableFuture.supplyAsync(() -> {
            log.info("Sending in 25sec");
            try {
                Thread.sleep(25000l);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sendBatch.sendBatch();
            return null;
        });
    }
}
