# Implement EventStore

In this challenge, you will create a class that implements the `EventStore` 
interface.
 
```java
public interface EventStore {
    void insert(Event event);

    void removeAll(String type);

    EventIterator query(String type, long startTime, long endTime);
}
```

## Documentation:

1) For this challenge I've decided to use an external library (AssertJ) 
to better test composite objects (EventStore and EventIterator);
2) Used the "Mutex" object and "ReentrantLock" class and your respective library as well to ensure that the code was indeed thread-safety;
3) Use of Map data struct instead of lists to reduce algorithm's time complexity, especially in the removeAll() 
function and also making easier to filter data in the query() function;
4) The insertions were chosen to insert sorted, helping to make the queries; 
