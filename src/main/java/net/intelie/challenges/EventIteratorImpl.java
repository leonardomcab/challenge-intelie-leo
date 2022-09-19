package net.intelie.challenges;

// import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class EventIteratorImpl implements EventIterator {
    private List<Event> events;
    private int index;
    private final ReentrantLock mutex = new ReentrantLock();

    EventIteratorImpl(List<Event> events) {
        this.events = events;
        this.index = -1;
    }

    public boolean moveNext() {
        try {
            this.mutex.lock();
            this.index++;
            return this.index < this.events.size();
        } finally {
            this.mutex.unlock();
        }
    }

    public Event current() {
        try {
            this.mutex.lock(); //
            return this.events.get(this.index);
        } catch(IndexOutOfBoundsException e) {
            throw new IllegalStateException("Element is trying to access a new position");
        } finally {
            this.mutex.unlock();
        }
    }

    public void remove() {
        try {
            this.mutex.lock();
            this.events.remove(this.index--);
        } catch(IndexOutOfBoundsException e) {
            throw new IllegalStateException("Element is trying to access a new position");
        } finally {
            this.mutex.unlock();
        }
    }

    public void close() throws Exception {
        try {
            this.mutex.lock();
            this.events = null; // Events are thrown away
        } finally {
            this.mutex.unlock();
        }
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}


