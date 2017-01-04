package com.gourd.erwa.lock.example;


import com.gourd.erwa.lock.Event;

import java.util.Collection;
import java.util.concurrent.locks.StampedLock;

/**
 * @author wei.Li
 */
public class Stamped extends AbsEventContainer {

    private final StampedLock lock = new StampedLock();

    @Override
    public Collection<Event> read() {

        long stamp = lock.readLock();
        try {
            return super.events;
        } finally {
            lock.unlockRead(stamp);
        }
    }

    @Override
    public void write(Event event) {

        long stamp = lock.writeLock();
        try {
            super.events.add(event);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

}
