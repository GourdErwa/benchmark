package com.gourd.erwa.lock.example;


import com.gourd.erwa.lock.Event;

import java.util.Collection;

/**
 * @author wei.Li
 */
public class Synchronize extends AbsEventContainer {

    private final Object lock = new Object();

    @Override
    public Collection<Event> read() {
        synchronized (lock) {
            return super.events;
        }
    }

    @Override
    public void write(Event event) {
        synchronized (lock) {
            super.events.add(event);
        }
    }

}
