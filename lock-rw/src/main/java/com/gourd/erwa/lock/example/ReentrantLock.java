package com.gourd.erwa.lock.example;


import com.gourd.erwa.lock.Event;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wei.Li
 */
public class ReentrantLock extends AbsEventContainer {

    private final ReadWriteLock rw = new ReentrantReadWriteLock();
    private final Lock r = rw.readLock();
    private final Lock w = rw.writeLock();

    @Override
    public Collection<Event> read() {

        r.lock();
        try {
            return super.events;
        } finally {
            r.unlock();
        }
    }

    @Override
    public void write(Event event) {

        w.lock();
        try {
            super.events.add(event);

        } finally {
            w.unlock();
        }
    }
}
