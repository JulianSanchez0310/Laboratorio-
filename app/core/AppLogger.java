package app.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Singleton logger that writes to both console and logs/logs.log.
 * Log level is controlled by the LOG_LEVEL key in .env (default: INFO).
 * Usage: AppLogger.getInstance().info("message")
 */
public class AppLogger {

    private static final String LOG_DIR  = "logs";
    private static final String LOG_FILE = "logs/logs.log";
    private static final String LOG_FORMAT = "[%1$tF %1$tT] [%4$-7s] %5$s%n";

    private static AppLogger instance;
    private final Logger logger;

    private AppLogger() {
        System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
        logger = Logger.getLogger("DataStructuresLab");
        logger.setUseParentHandlers(false);

        String levelName = AppConfig.getInstance().get("LOG_LEVEL", "INFO");
        Level level = parseLevel(levelName);
        logger.setLevel(level);

        addConsoleHandler(level);
        addFileHandler(level);
    }

    public static AppLogger getInstance() {
        if (instance == null) {
            instance = new AppLogger();
        }
        return instance;
    }

    public void info(String message)    { logger.info(message); }
    public void warning(String message) { logger.warning(message); }
    public void severe(String message)  { logger.severe(message); }
    public void debug(String message)   { logger.fine(message); }

    private void addConsoleHandler(Level level) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        consoleHandler.setLevel(level);
        logger.addHandler(consoleHandler);
    }

    private void addFileHandler(Level level) {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
            FileHandler fileHandler = new FileHandler(LOG_FILE, false);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(level);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.warning("Could not create file handler for " + LOG_FILE + ": " + e.getMessage());
        }
    }

    private Level parseLevel(String levelName) {
        try {
            return Level.parse(levelName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Level.INFO;
        }
    }
}
