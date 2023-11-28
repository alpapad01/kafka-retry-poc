package com.eshare.kafka.handler.evthanlder.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshare.kafka.handler.config.KafkaTopicConfig;
import com.eshare.kafka.handler.evthanlder.TaskEventHandler;
import com.eshare.kafka.handler.evthanlder.TerminalException;
import com.eshare.kafka.handler.evthanlder.TransientException;
import com.eshare.kafka.handler.model.TaskEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventHandlerImpl implements TaskEventHandler {

    @Autowired
    @Qualifier("kafkaTemplate")
    private KafkaTemplate<String, Object> template;
    
    @Override
    @Transactional(transactionManager="kafkaTransactionManager",propagation = Propagation.REQUIRES_NEW)
    public void onEvent(TaskEvent event) throws Exception {
        log.info("On event {}", event);
        // if terminal or transient, should not commit
        template.send(KafkaTopicConfig.COMPLETED_TOPIC, event.partition(), event.id(), event);
        
        if (event.failTerminal()) {
            throw new TerminalException("Teminal error");
        }
        if (event.failTransient()) {
            throw new TransientException("Transient error");
        }
        if (event.delay() > 0) {
             Thread.sleep(event.delay());
        }
    }

    @Override
    @Transactional(transactionManager="kafkaTransactionManager",propagation = Propagation.REQUIRES_NEW)
    public void onRequeueEvent(TaskEvent event) {
        log.info("On re-queue-event {}", event);
    }

}
