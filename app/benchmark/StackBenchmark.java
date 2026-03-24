package app.benchmark;

import java.util.Random;

import app.core.AppLogger;
import app.datastructures.stack.ArrayStack;

public class StackBenchmark {

    private static final String NAME = "ArrayStack";
    private static volatile int sink = 0;

    public static void run(int[] sizes, int repetitions) {
        Random rand = new Random(42);
        for (int size : sizes) {
            AppLogger.getInstance().info("Stack benchmark — size: " + size);
            System.out.println("\n=== SIZE: " + size + " ===");
            runFor(size, repetitions, rand);
        }
    }

    private static void runFor(int size, int repetitions, Random rand) {
        ArrayStack<Integer> stack = new ArrayStack<>();

        // push — average per operation
        long totalPush = Benchmark.measure(() -> {
            for (int i = 0; i < size; i++) stack.push(rand.nextInt(1_000_000));
        });
        long perPush = size > 0 ? totalPush / size : 0;
        Benchmark.print("push", perPush);
        ResultCollector.add(NAME, "push", size, perPush);

        Benchmark.trackAverage(NAME, "peek",    size, () -> sink ^= stack.peek(),             repetitions);
        Benchmark.trackAverage(NAME, "size",    size, () -> sink ^= stack.size(),             repetitions);
        Benchmark.trackAverage(NAME, "isEmpty", size, () -> sink ^= (stack.isEmpty() ? 1 : 0), repetitions);

        // FIX 3 — delete: use the bottom element as the target, not peek() (top).
        // If delete does a linear scan, peek() gives best-case O(1) behaviour and
        // hides the real O(n) cost. Pushing a known sentinel at the very beginning
        // (before the stack is filled) guarantees it sits at the bottom and forces
        // the worst-case traversal.
        ArrayStack<Integer> stackForDelete = new ArrayStack<>();
        int sentinel = -1;              // a value that will not appear in random data
        stackForDelete.push(sentinel);  // goes to the bottom
        for (int i = 0; i < size; i++) stackForDelete.push(rand.nextInt(1_000_000));
        Benchmark.track(NAME, "delete", size, () -> stackForDelete.delete(sentinel));

        
        // pop — keep stack size stable during the average measurement
        Benchmark.trackAverage(NAME, "pop", size, () -> {
            sink ^= stack.pop();
            stack.push(rand.nextInt());
        }, repetitions);

        sink = 0;
    }
}

