package com.gourd.erwa.lock.example;

import com.gourd.erwa.lock.Event;
import com.gourd.erwa.lock.EventContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 深入理解Java内存模型（四）——volatile
 * http://ifeve.com/java-memory-model-4/
 *
 * @author wei.Li
 */
public class Volatile implements EventContainer {

    private volatile List<Event> events = new ArrayList<>();

    @Override
    public Collection<Event> read() {
        return this.events;
    }

    @Override
    public void write(Event event) {
        this.events.add(event);
    }

}
