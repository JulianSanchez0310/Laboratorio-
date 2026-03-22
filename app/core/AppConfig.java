package app.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton that loads configuration from a .env file at project root.
 * Falls back to system environment variables if a key is not found in .env.
 * Usage: AppConfig.getInstance().get("MY_KEY", "default")
 */
public class AppConfig {

    private static final String ENV_FILE = ".env";
    private static AppConfig instance;

    private final Map<String, String> properties = new HashMap<>();

    private AppConfig() {
        loadEnvFile();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    /** Returns the value for key, or null if not found. */
    public String get(String key) {
        String value = properties.get(key);
        return value != null ? value : System.getenv(key);
    }

    /** Returns the value for key, or defaultValue if not found. */
    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    /** Returns the integer value for key, or defaultValue if not found or not parseable. */
    public int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void loadEnvFile() {
        try {
            Files.lines(Paths.get(ENV_FILE))
                .map(String::trim)
                .filter(line -> !line.startsWith("#") && line.contains("="))
                .forEach(line -> {
                    int separatorIndex = line.indexOf('=');
                    String key   = line.substring(0, separatorIndex).trim();
                    String value = line.substring(separatorIndex + 1).trim();
                    properties.put(key, value);
                });
        } catch (IOException e) {
            // .env is optional; missing file is not an error
        }
    }
}
