package com.eshare.kafka.handler.evthanlder;

import com.eshare.kafka.handler.model.TaskEvent;


public interface TaskEventHandler {
    void onEvent(TaskEvent event) throws Exception;

    void onRequeueEvent(TaskEvent event);
}
