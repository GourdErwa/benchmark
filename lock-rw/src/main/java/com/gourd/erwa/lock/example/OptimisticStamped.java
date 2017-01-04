package com.gourd.erwa.lock.example;


import com.gourd.erwa.lock.Event;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.StampedLock;

/**
 * @author wei.Li
 */
public class OptimisticStamped extends AbsEventContainer {

    private final StampedLock lock = new StampedLock();

    @Override
    public Collection<Event> read() {

        //获得一个乐观读锁
        long stamp = lock.tryOptimisticRead();
        List<Event> result = super.events;
        //检查发出乐观读锁后同时是否有其他写锁发生？
        if (!lock.validate(stamp)) {
            //如果没有，我们再次获得一个读悲观锁
            stamp = lock.readLock();
            try {
                result = super.events;
            } finally {
                lock.unlockRead(stamp);
            }
        }

        return result;
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
