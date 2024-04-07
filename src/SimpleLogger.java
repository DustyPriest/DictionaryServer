// SimpleLogger
// Interface for sending updates to the server log

public interface SimpleLogger {
    void updateLog(String message);

    void incrementClientCounter();
    void decrementClientCounter();
}
