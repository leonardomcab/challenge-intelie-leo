package net.intelie.challenges;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EventIteratorTest {
    // Testing with 2 elements in moveNext, first one is true and the second is false.
    @Test
    public void eventIterator_moveNextFirstElement() throws Exception {
        Event event = new Event("some_type", 123L);

        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);

        // Testing "query" EventIterator:
        EventIterator expectedEventIterator = new EventIteratorImpl(expectedList);

        assertThat(expectedEventIterator.moveNext()).isTrue();
        assertThat(expectedEventIterator.moveNext()).isFalse();
    }

    @Test(expected = IllegalStateException.class)
    public void eventIterator_currentInAnInvalidIndex() throws Exception {
        Event event = new Event("some_type", 123L);

        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);

        // Testing "query" EventIterator:
        EventIterator expectedEventIterator = new EventIteratorImpl(expectedList);

        expectedEventIterator.current();
    }

    @Test
    public void eventIterator_currentInAValidIndex() throws Exception {
        Event event = new Event("some_type", 123L);

        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);

        // Testing "query" EventIterator:
        EventIteratorImpl expectedEventIterator = new EventIteratorImpl(expectedList);

        expectedEventIterator.setIndex(0);
        assertThat(expectedEventIterator.current()).usingRecursiveComparison().isEqualTo(event);
    }

    @Test
    public void eventIterator_removeInAValidIndex() throws Exception {
        Event event = new Event("some_type", 123L);

        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);

        // Testing "query" EventIterator:
        EventIteratorImpl expectedEventIterator = new EventIteratorImpl(expectedList);

        expectedEventIterator.setIndex(0);
        expectedEventIterator.remove();
        assertThat(expectedEventIterator.getEvents()).isEmpty();
        assertThat(expectedEventIterator.getIndex()).isEqualTo(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void eventIterator_removeInAnInvalidIndex() throws Exception {
        Event event = new Event("some_type", 123L);

        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);

        // Testing "query" EventIterator:
        EventIteratorImpl expectedEventIterator = new EventIteratorImpl(expectedList);

        expectedEventIterator.remove();
    }

    @Test
    public void eventIterator_close() throws Exception {
        Event event = new Event("some_type", 123L);

        LinkedList<Event> expectedList = new LinkedList<>();
        expectedList.add(event);

        // Testing "query" EventIterator:
        EventIteratorImpl expectedEventIterator = new EventIteratorImpl(expectedList);

        assertThat(expectedEventIterator.getEvents()).isNotNull();
        expectedEventIterator.close();
        assertThat(expectedEventIterator.getEvents()).isNull();
    }
}
