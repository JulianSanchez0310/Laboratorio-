package app.benchmark;

public class Benchmark {

    // ── Raw timing ────────────────────────────────────────────────────────────

    public static long measure(Runnable task) {
        long start = System.nanoTime();
        task.run();
        return System.nanoTime() - start;
    }

    public static long measureAverage(Runnable task, int repetitions) {
        long start = System.nanoTime();
        for (int i = 0; i < repetitions; i++) task.run();
        return (System.nanoTime() - start) / repetitions;
    }

    // ── Measure + print + record (keeps all three in sync) ───────────────────

    /**
     * Runs task once, prints the elapsed time, and records it in ResultCollector.
     * Returns nanoseconds elapsed.
     */
    public static long track(String structure, String operation, int size, Runnable task) {
        long t = measure(task);
        print(operation, t);
        ResultCollector.add(structure, operation, size, t);
        return t;
    }

    /**
     * Runs task reps times, prints the average, and records it in ResultCollector.
     * Returns average nanoseconds per call.
     */
    public static long trackAverage(String structure, String operation, int size,
                                    Runnable task, int reps) {
        long t = measureAverage(task, reps);
        print(operation + " (avg)", t);
        ResultCollector.add(structure, operation, size, t);
        return t;
    }

    // ── Console output ────────────────────────────────────────────────────────

    public static void print(String label, long nanoseconds) {
        System.out.printf("  %-26s %,d ns%n", label + ":", nanoseconds);
    }
}
