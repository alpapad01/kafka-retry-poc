package com.eshare.kafka.handler.evthanlder;

public class TransientException extends RuntimeException {

    public TransientException(String string) {
        super(string);
    }

    private static final long serialVersionUID = 1L;

}
