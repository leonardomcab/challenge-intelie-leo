package net.intelie.challenges;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EventStoreTest {
    @Test
    public void eventStore_queryWithFilledIterator() throws Exception {
        Event event = new Event("some_type", 123L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);
        eventMap.put("some_type", expectedList);
        eventStore.setEvents(eventMap);

        // Testing "query" EventIterator:
        EventIterator expectedEventIterator = new EventIteratorImpl(expectedList);

        assertThat(eventStore.query("some_type", 100L, 200L)).usingRecursiveComparison().ignoringFields("mutex").isEqualTo(expectedEventIterator);
    }

    // Inserting an element
    @Test
    public void eventStore_queryWithNullIteratorDueToWrongTimestamp() throws Exception {
        Event event = new Event("some_type", 123L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);
        eventMap.put("some_type", expectedList);
        eventStore.setEvents(eventMap);

        assertThat(eventStore.query("some_type", 180L, 200L)).isNull();
    }

    @Test
    public void eventStore_queryWithNullIteratorDueToWrongKey() throws Exception {
        Event event = new Event("some_type", 123L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);
        eventMap.put("some_type", expectedList);
        eventStore.setEvents(eventMap);

        assertThat(eventStore.query("some_type2", 100L, 200L)).isNull();
    }

    // Creating a map setting the events on the EventStore - proving that method removeAll is
    @Test
    public void eventStore_removeAllWithExistingKey() throws Exception {
        Event event = new Event("some_type", 123L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);
        eventMap.put("some_type", expectedList);
        eventStore.setEvents(eventMap);

        assertThat(eventStore.getEvents()).usingRecursiveComparison().isEqualTo(eventMap);

        eventStore.removeAll("some_type");

        assertThat(eventStore.getEvents()).isEmpty();
    }

    // Here we cannot delete "some_type_2" because it don't exist
    @Test
    public void eventStore_removeAllWithNonExistingKey() throws Exception {
        Event event = new Event("some_type", 123L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);
        eventMap.put("some_type", expectedList);
        eventStore.setEvents(eventMap);

        assertThat(eventStore.getEvents()).usingRecursiveComparison().isEqualTo(eventMap);

        eventStore.removeAll("some_type_2");

        assertThat(eventStore.getEvents()).isNotEmpty();
    }

    @Test
    public void eventStore_insert() throws Exception {
        Event event = new Event("some_type", 123L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);
        eventMap.put("some_type", expectedList);

        eventStore.insert(event);

        assertThat(eventStore.getEvents()).usingRecursiveComparison().isEqualTo(eventMap);
    }

    // Inserting a element at the end of the list in the specific order: 1) event, 2) event2
    @Test
    public void eventStore_insertAtTheEnd() throws Exception {
        Event event = new Event("some_type", 123L);
        Event event2 = new Event("some_type", 130L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();

        expectedList.add(event);
        expectedList.add(event2);

        eventMap.put("some_type", expectedList);

        eventStore.insert(event);
        eventStore.insert(event2);

        assertThat(eventStore.getEvents()).usingRecursiveComparison().isEqualTo(eventMap);
    }

    @Test
    public void eventStore_insertAtTheBeginning() throws Exception {
        Event event = new Event("some_type", 123L);
        Event event2 = new Event("some_type", 130L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();

        expectedList.add(event);
        expectedList.add(event2);

        eventMap.put("some_type", expectedList);

        eventStore.insert(event2);
        eventStore.insert(event);

        assertThat(eventStore.getEvents()).usingRecursiveComparison().isEqualTo(eventMap);
    }

    @Test
    public void eventStore_insertADifferentKey() throws Exception {
        Event event = new Event("some_type", 123L);
        Event event2 = new Event("some_type2", 130L);
        EventStoreImpl eventStore = new EventStoreImpl();

        HashMap<String, List<Event>> eventMap = new HashMap<>();
        LinkedList<Event> expectedList = new LinkedList<>();

        expectedList.add(event);

        eventMap.put("some_type", expectedList);

        LinkedList<Event> expectedList2 = new LinkedList<>();

        expectedList2.add(event2);

        eventMap.put("some_type2", expectedList2);

        eventStore.insert(event2);
        eventStore.insert(event);

        assertThat(eventStore.getEvents()).usingRecursiveComparison().isEqualTo(eventMap);
    }

}
