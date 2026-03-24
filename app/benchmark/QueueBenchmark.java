package app.benchmark;

import java.util.Random;

import app.core.AppLogger;
import app.datastructures.queue.ArrayQueue;

public class QueueBenchmark {

    private static final String NAME = "ArrayQueue";
    private static volatile int sink = 0;

    public static void run(int[] sizes, int repetitions) {
        Random rand = new Random(42);
        for (int size : sizes) {
            AppLogger.getInstance().info("Queue benchmark — size: " + size);
            System.out.println("\n=== SIZE: " + size + " ===");
            runFor(size, repetitions, rand);
        }
    }

    private static void runFor(int size, int repetitions, Random rand) {
        ArrayQueue<Integer> queue = new ArrayQueue<>();

        // enqueue — amortised O(1) on array-backed structure; averaging N ops is valid here.
        long totalEnq = Benchmark.measure(() -> {
            for (int i = 0; i < size; i++) queue.enqueue(rand.nextInt(1_000_000));
        });
        long perEnq = size > 0 ? totalEnq / size : 0;
        Benchmark.print("enqueue", perEnq);
        ResultCollector.add(NAME, "enqueue", size, perEnq);

        Benchmark.trackAverage(NAME, "front",   size, () -> sink ^= (Integer) queue.front(),    repetitions);
        Benchmark.trackAverage(NAME, "size",    size, () -> sink ^= queue.size(),               repetitions);
        Benchmark.trackAverage(NAME, "isEmpty", size, () -> sink ^= (queue.isEmpty() ? 1 : 0), repetitions);

        // FIX 3 — delete: use the back element as the target, not front().
        // front() is the cheapest element to find if delete scans from the front,
        // giving best-case O(1) and hiding the real O(n) cost.
        // We enqueue a sentinel LAST so it sits at the back, forcing a full traversal.
        ArrayQueue<Integer> queueForDelete = new ArrayQueue<>();
        for (int i = 0; i < size; i++) queueForDelete.enqueue(rand.nextInt(1_000_000));
        int sentinel = -1;              // a value that will not appear in random data
        queueForDelete.enqueue(sentinel); // goes to the back
        Benchmark.track(NAME, "delete", size, () -> queueForDelete.delete(sentinel));

        // dequeue — keep queue size stable during the average measurement.
        Benchmark.trackAverage(NAME, "dequeue", size, () -> {
            sink ^= (Integer) queue.dequeue();
            queue.enqueue(rand.nextInt());
        }, repetitions);

        sink = 0;
    }
}
