package com.eshare.kafka.handler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

public record TaskEvent(@JsonProperty("theId") String id, boolean failTerminal, boolean failTransient, long delay, int order, int partition) {

    @Builder
    public TaskEvent {
    }
    
}
