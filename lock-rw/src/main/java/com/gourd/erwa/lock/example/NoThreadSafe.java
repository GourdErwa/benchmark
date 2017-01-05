package com.gourd.erwa.lock.example;


import com.gourd.erwa.lock.Event;

import java.util.Collection;

/**
 * @author wei.Li
 */
public class NoThreadSafe extends AbsEventContainer {

    @Override
    public Collection<Event> read() {

        return super.events;
    }

    @Override
    public void write(Event event) {

        super.events.add(event);

    }
}
