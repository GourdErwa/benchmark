package com.gourd.erwa.lock;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * 读写操作
 *
 * @author wei.Li
 */
abstract class Operation implements Callable<Boolean> {

    private EventContainer eventContainer;
    private RunCondition runCondition;


    Operation(EventContainer eventContainer, RunCondition runCondition) {
        this.eventContainer = eventContainer;
        this.runCondition = runCondition;
    }


    public static class Reader extends Operation {


        Reader(EventContainer eventContainer, RunCondition runCondition) {
            super(eventContainer, runCondition);
        }

        @Override
        public Boolean call() throws Exception {

            while (true) {

                if (Thread.interrupted()) {
                    break;
                }

                final Collection<Event> events = super.eventContainer.read();

                if (events.size() > super.runCondition.getFullNum()) {
                    break;
                }
            }
            return true;
        }
    }

    public static class Writer extends Operation {


        Writer(EventContainer eventContainer, RunCondition runCondition) {
            super(eventContainer, runCondition);
        }

        @Override
        public Boolean call() throws Exception {

            while (true) {
                if (Thread.interrupted()) {
                    break;
                }
                super.eventContainer.write(new Event() {
                });
            }
            return true;
        }
    }
}
