package com.gourd.erwa.lock.example;


import com.gourd.erwa.lock.Event;

import java.util.Collection;
import java.util.concurrent.locks.Lock;

/**
 * @author wei.Li
 */
public class ReentrantLock extends AbsEventContainer {

    private final Lock lock = new java.util.concurrent.locks.ReentrantLock();

    @Override
    public Collection<Event> read() {

        lock.lock();
        try {
            return super.events;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void write(Event event) {

        lock.lock();
        try {
            super.events.add(event);

        } finally {
            lock.unlock();
        }
    }
}
