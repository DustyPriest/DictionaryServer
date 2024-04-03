public interface SimpleLogger {
    void updateLog(String message);

    void incrementClientCounter();
    void decrementClientCounter();
}
