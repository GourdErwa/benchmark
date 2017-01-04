package com.gourd.erwa.lock.example;


import com.gourd.erwa.lock.Event;
import com.gourd.erwa.lock.EventContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wei.Li
 */
abstract class AbsEventContainer implements EventContainer {

    final List<Event> events = new ArrayList<>();

}
