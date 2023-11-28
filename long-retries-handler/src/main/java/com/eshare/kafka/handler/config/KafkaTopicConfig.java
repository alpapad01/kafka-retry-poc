package com.eshare.kafka.handler.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class KafkaTopicConfig {
    public static final String TASKS_TOPIC = "tasks";
    public static final String COMPLETED_TOPIC = "completed";
    public static final String TASKS_GROUP_ID = "tasks-consumer-group";


    private final KafkaProperties properties;
    
    @Bean
    public NewTopic tasksTopic() {
        return new NewTopic(TASKS_TOPIC, 27, (short) 3);
    }
    
    @Bean
    public NewTopic completedTopic() {
        return new NewTopic(COMPLETED_TOPIC, 27, (short) 3);
    }

    @Bean("kafkaTemplate")
    @Primary
    public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,
            ProducerListener<Object, Object> kafkaProducerListener,
            ObjectProvider<RecordMessageConverter> messageConverter) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
        map.from(this.properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(this.properties.getTemplate().getTransactionIdPrefix()).to(kafkaTemplate::setTransactionIdPrefix);
        map.from(this.properties.getTemplate().isObservationEnabled()).to(kafkaTemplate::setObservationEnabled);
        return kafkaTemplate;
    }

    @Bean("kafkaRetryTemplate")
    public KafkaTemplate<?, ?> kafkaRetryTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,
            ProducerListener<Object, Object> kafkaProducerListener,
            ObjectProvider<RecordMessageConverter> messageConverter) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
        map.from(this.properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(this.properties.getTemplate().isObservationEnabled()).to(kafkaTemplate::setObservationEnabled);
        //kafkaTemplate.setTransactionIdPrefix("tata-");
        kafkaTemplate.setTransactionIdPrefix(null);
        kafkaTemplate.setDefaultTopic("nada");
        return kafkaTemplate;
    }
}
