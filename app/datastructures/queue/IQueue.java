package app.datastructures.queue;

public interface IQueue<T> {
    void enqueue(T element);
    T dequeue();
    T front();
    boolean isEmpty();
    int size();
    void delete(T element);
}
