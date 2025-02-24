package gilu.command;

/**
 * Represents the different commands supported by Gilu.
 */
public enum Command {
    LIST, SORT, LIST_DATE, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, EXIT, UNKNOWN;

    /**
     * Converts user input into a corresponding Command enum.
     *
     * @param input The raw user input string.
     * @return The corresponding Command enum.
     */
    public static Command fromInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        // Extract the first word from input (command)
        String[] words = input.trim().split("\\s+", 2);  // Split on first space only
        String command = words[0].toLowerCase();  // Normalize to lowercase

        switch (command) {
            case "list":
                return LIST;
            case "sort":
                return SORT;
            case "list_date":
                return LIST_DATE;
            case "mark":
                return MARK;
            case "unmark":
                return UNMARK;
            case "delete":
                return DELETE;
            case "todo":
                return TODO;
            case "deadline":
                return DEADLINE;
            case "event":
                return EVENT;
            case "find":
                return FIND;
            case "bye":
                return EXIT;
            default:
                return UNKNOWN;  // Unknown commands
        }
    }
}
