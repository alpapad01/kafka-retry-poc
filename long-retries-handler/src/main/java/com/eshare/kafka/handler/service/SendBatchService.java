package com.eshare.kafka.handler.service;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eshare.kafka.handler.config.KafkaTopicConfig;
import com.eshare.kafka.handler.model.TaskEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SendBatchService {

    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate<String, Object> template;
    private final Random random = new Random();

    @Transactional("kafkaTransactionManager")
    public void sendBatch() {
        for (int i = 0; i < 27; i++) {
            try {
            var partition = i % 27;
            var event = TaskEvent.builder()
                    .id(UUID.randomUUID().toString())
                    .failTerminal(i % 3 == 0)
                    .failTransient(i % 3 == 1)
                    .delay(getRandomNumberUsingInts(1000, 2000))
                    .order(i)
                    .partition(partition)
                    .build();
            log.info("Sending: {}", event);
            template.send(KafkaTopicConfig.TASKS_TOPIC, partition, event.id(), event);
            } catch(Exception e) {
                log.error("Error sending record", e);
            }
        }

    }

    public int getRandomNumberUsingInts(int min, int max) {
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }
}
