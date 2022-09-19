package net.intelie.challenges;

import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class EventStoreImpl implements EventStore {
    private Map<String, List<Event>> events = new HashMap<>();
    private final ReentrantLock mutex = new ReentrantLock();

    EventStoreImpl() {
        this.events = new HashMap<String, List<Event>>();
    }

    // Getting the list specifically for the tests
    public Map<String, List<Event>> getEvents() {
        return this.events;
    }

    // Setting the list specifically for the tests
    public void setEvents(Map<String, List<Event>> events) {
        this.events = events;
    }

    public void insert(Event event) {
        try {
            this.mutex.lock();
            List<Event> eventList = this.events.get(event.type());
            if (eventList == null) {
                List<Event> list = new LinkedList<>();
                list.add(event);
                this.events.put(event.type(), list);
                return;
            }
            for (int i = 0; i < eventList.size(); i++) {
                if (eventList.get(i).timestamp() > event.timestamp()) {
                    eventList.add(i, event);
                    return;
                }
            }
            eventList.add(event); //
        } finally {
            this.mutex.unlock();
        }
    }

    public void removeAll(String type) {
        try {
            this.mutex.lock();
            this.events.remove(type);
        } finally {
            this.mutex.unlock();
        }
    }

    public EventIterator query(String type, long startTime, long endTime) {
        try {
            this.mutex.lock();
            List<Event> eventList = this.events.get(type);
            List<Event> returnedList = new LinkedList<>();
            if (eventList == null) {
               return null;
            }
            for (Event tempEvent : eventList) {
                long timestamp = tempEvent.timestamp();
                if (timestamp >= startTime && timestamp < endTime) {
                    returnedList.add(tempEvent);
                }
            }
            if (returnedList.isEmpty()) {
                return null;
            }
            EventIterator iterator = new EventIteratorImpl(returnedList);
            return iterator;
        } finally {
            this.mutex.unlock();
        }
    }
}
