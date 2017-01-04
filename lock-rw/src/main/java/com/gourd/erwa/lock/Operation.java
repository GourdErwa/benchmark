package com.gourd.erwa.lock;

import java.util.Collection;

/**
 * 读写操作
 *
 * @author wei.Li
 */
abstract class Operation implements Runnable {

    private EventContainer eventContainer;
    private RunCondition runCondition;


    Operation(EventContainer eventContainer, RunCondition runCondition) {
        this.eventContainer = eventContainer;
        this.runCondition = runCondition;
    }

    private void checkStop(int size) {

        if (size < this.runCondition.getFullNum()) {
            return;
        }
        Main.stop(EventContainerType.forClass(this.eventContainer.getClass()));
    }

    public static class Reader extends Operation {


        Reader(EventContainer eventContainer, RunCondition runCondition) {
            super(eventContainer, runCondition);
        }

        @Override
        public void run() {

            while (true) {

                if (Thread.interrupted()) {
                    break;
                }

                final Collection<Event> events = super.eventContainer.read();

                super.checkStop(events.size());

            }
        }
    }

    public static class Writer extends Operation {


        Writer(EventContainer eventContainer, RunCondition runCondition) {
            super(eventContainer, runCondition);
        }

        @Override
        public void run() {

            while (true) {
                if (Thread.interrupted()) {
                    break;
                }
                super.eventContainer.write(new Event() {
                });
            }
        }
    }
}
