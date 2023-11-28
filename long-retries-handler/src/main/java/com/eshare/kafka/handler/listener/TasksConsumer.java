package com.eshare.kafka.handler.listener;

import static com.eshare.kafka.handler.config.KafkaTopicConfig.TASKS_TOPIC;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eshare.kafka.handler.config.KafkaTopicConfig;
import com.eshare.kafka.handler.evthanlder.TaskEventHandler;
import com.eshare.kafka.handler.evthanlder.TerminalException;
import com.eshare.kafka.handler.model.TaskEvent;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
@RequiredArgsConstructor
@Observed
public class TasksConsumer {

    private final TaskEventHandler taskEventHandler;
    
    @RetryableTopic(kafkaTemplate = "kafkaRetryTemplate",
            exclude = {DeserializationException.class,
                    MessageConversionException.class,
                    ConversionException.class,
                    MethodArgumentResolutionException.class,
                    NoSuchMethodException.class,
                    ClassCastException.class,
                    TerminalException.class},
            attempts = "4",
            numPartitions = "27",
            backoff = @Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000)
    )
    @KafkaListener(topics = TASKS_TOPIC, groupId = KafkaTopicConfig.TASKS_GROUP_ID, concurrency = "28")
    //@Transactional
    public void taskEventListener(TaskEvent taskEvent, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition ) throws Exception {
        log.info("topic: '{}' status handler receive data = {}", topic, taskEvent);
        taskEventHandler.onEvent(taskEvent);
        ack.acknowledge();
    }

    @DltHandler
    //@Transactional
    public void processMessage(MessageHeaders headers, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, TaskEvent message) {
        log.error("topic: '{}' DltHandler processMessage = {}, headers={}", topic, message, headers);
    }
    
}