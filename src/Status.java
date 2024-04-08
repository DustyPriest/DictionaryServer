// Status
// Enum for consistent message statuses b/n client & server
// Field of NetworkMessage

public enum Status {
    FAILURE_NOT_FOUND("Word not found in dictionary"),
    FAILURE_WORD_EXISTS("Word already exists in dictionary"),
    FAILURE_INVALID_INPUT("Invalid input"),
    FAILURE_DEFINITION_EXISTS("Inputted definition already exists for that word in the dictionary"),
    SUCCESS_WORD_ADDED("Word added successfully"),
    SUCCESS_WORD_FOUND("Word found successfully"),
    SUCCESS_WORD_REMOVED("Word removed successfully"),
    SUCCESS_WORD_UPDATED("Word updated successfully"),
    TASK_QUERY("Querying dictionary"),
    TASK_ADD("Adding word to dictionary"),
    TASK_REMOVE("Removing word from dictionary"),
    TASK_UPDATE("Updating word in dictionary");

    private final String message;

    private Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
