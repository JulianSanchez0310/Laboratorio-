package app.benchmark;

import app.core.AppConfig;
import app.core.AppLogger;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    private static final String CSV_PATH = "results/benchmark_results.csv";

    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();
        AppLogger  logger = AppLogger.getInstance();

        int[] sizes       = parseSizes(config.get("BENCHMARK_SIZES", "10,100,10000,1000000,100000000"));
        int   repetitions = config.getInt("BENCHMARK_REPETITIONS", 100);

        logger.info("Benchmark starting — sizes: " + Arrays.toString(sizes)
                    + ", repetitions: " + repetitions);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      DATA STRUCTURES BENCHMARK       ║");
        System.out.println("╚══════════════════════════════════════╝");

        // ── Timing (nothing else runs inside these calls) ─────────────────────
        System.out.println("\n\n━━━  LINKED LISTS  ━━━");
        ListBenchmark.run(sizes, repetitions);

        System.out.println("\n\n━━━  STACK  ━━━");
        StackBenchmark.run(sizes, repetitions);

        System.out.println("\n\n━━━  QUEUE  ━━━");
        QueueBenchmark.run(sizes, repetitions);

        // ── CSV export (after all timing is done) ─────────────────────────────
        try {
            ResultCollector.exportCsv(CSV_PATH);
            logger.info("Results written to " + CSV_PATH);
            System.out.println("\nResultados guardados en: " + CSV_PATH);
        } catch (IOException e) {
            logger.severe("Could not write CSV: " + e.getMessage());
        }

        logger.info("Benchmark completed.");
    }

    private static int[] parseSizes(String sizesStr) {
        String[] parts = sizesStr.split(",");
        int[] sizes = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            sizes[i] = Integer.parseInt(parts[i].trim());
        }
        return sizes;
    }
}
