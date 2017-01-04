package com.gourd.erwa.lock;

import java.util.Collection;

/**
 * @author wei.Li
 */
public interface EventContainer {

    Collection<Event> read();

    void write(Event event);

}
