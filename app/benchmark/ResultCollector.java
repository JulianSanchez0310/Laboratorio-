package app.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Collects benchmark results in memory and exports them to CSV.
 * The CSV is written AFTER all timing has finished, keeping
 * measurement and I/O completely separate.
 */
public class ResultCollector {

    private static final List<long[]> numericRows = new ArrayList<>();
    private static final List<String[]> labelRows  = new ArrayList<>();

    private ResultCollector() {}

    /**
     * Records a single measured result.
     *
     * @param structure  e.g. "SinglyLinkedList", "ArrayStack"
     * @param operation  e.g. "pushFront", "enqueue"
     * @param size       input size used in this measurement
     * @param timeNs     elapsed nanoseconds
     */
    public static void add(String structure, String operation, int size, long timeNs) {
        labelRows.add(new String[]{structure, operation});
        numericRows.add(new long[]{size, timeNs});
    }

    /**
     * Writes all collected results to a CSV file.
     * Columns: structure, operation, size, time_ns, time_us
     */
    public static void exportCsv(String filePath) throws IOException {
        Files.createDirectories(Paths.get(filePath).getParent());
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("structure,operation,size,time_ns,time_us");
            for (int i = 0; i < labelRows.size(); i++) {
                String[] label   = labelRows.get(i);
                long[]   numeric = numericRows.get(i);
                double   us      = numeric[1] / 1_000.0;
                // Locale.US ensures '.' as decimal separator, keeping the CSV valid
                writer.printf(Locale.US, "%s,%s,%d,%d,%.3f%n",
                        label[0], label[1], numeric[0], numeric[1], us);
            }
        }
    }
}
