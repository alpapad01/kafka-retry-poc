package com.eshare.kafka.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableKafka
public class LongTaskHandlerPocApplication {
    public static void main(String[] args) {
        SpringApplication.run(LongTaskHandlerPocApplication.class, args);
    }
}
