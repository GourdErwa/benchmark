package com.gourd.erwa.lock;


import com.gourd.erwa.lock.example.*;

import java.util.Arrays;

/**
 * 支持读写实现类
 *
 * @author wei.Li
 */
public enum EventContainerType {

    READ_WRITE_LOCK(ReadWriteLock.class),

    REENTRANT_LOCK(ReentrantLock.class),

    STAMPED(Stamped.class),

    OPTIMISTIC_STAMPED(OptimisticStamped.class),

    SYNCHRONIZE(Synchronize.class),

    VOLATILE(Volatile.class);

    private Class<? extends EventContainer> aClass;

    EventContainerType(Class<? extends EventContainer> aClass) {
        this.aClass = aClass;
    }

    public static EventContainerType forClass(Class<? extends EventContainer> aClass) {

        return Arrays.stream(EventContainerType.values()).filter(c -> c.aClass.equals(aClass)).findFirst().orElse(null);
    }

    public Class<? extends EventContainer> getaClass() {
        return aClass;
    }

    public EventContainer getImplInstance() {
        try {
            return this.aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

}
