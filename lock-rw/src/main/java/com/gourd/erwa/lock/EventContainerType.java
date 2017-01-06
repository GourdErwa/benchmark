package com.gourd.erwa.lock;


import com.gourd.erwa.lock.example.*;

import java.util.Arrays;

/**
 * 支持读写实现类
 *
 * @author wei.Li
 */
public enum EventContainerType {

    READ_WRITE_LOCK("ReadWriteLock", ReadWriteLock.class),

    REENTRANT_LOCK("ReentrantLock", ReentrantLock.class),

    STAMPED("StampedLock", Stamped.class),

    OPTIMISTIC_STAMPED("OptimisticStamped", OptimisticStamped.class),

    SYNCHRONIZE("Synchronize", Synchronize.class),

    VOLATILE("Volatile", Volatile.class),

    NO_THREAD_SAFE("NoThreadSafe", NoThreadSafe.class);

    private String describe;
    private Class<? extends EventContainer> aClass;

    EventContainerType(String describe, Class<? extends EventContainer> aClass) {
        this.describe = describe;
        this.aClass = aClass;
    }

    public static EventContainerType forClass(Class<? extends EventContainer> aClass) {

        return Arrays.stream(EventContainerType.values()).filter(c -> c.aClass.equals(aClass)).findFirst().orElse(null);
    }

    public String getDescribe() {
        return describe;
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
