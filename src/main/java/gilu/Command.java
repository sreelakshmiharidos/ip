package gilu;

/**
 * Represents the different commands supported by Gilu.
 */
public enum Command {
    LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, EXIT, UNKNOWN;

    /**
     * Converts user input into a corresponding Command enum.
     *
     * @param input The raw user input string.
     * @return The corresponding Command enum.
     */
    public static Command fromInput(String input) {
        if (input.equalsIgnoreCase("list")) {
            return LIST;
        }
        if (input.startsWith("mark")) {
            return MARK;
        }
        if (input.startsWith("unmark")) {
            return UNMARK;
        }
        if (input.startsWith("delete")) {
            return DELETE;
        }
        if (input.startsWith("todo")) {
            return TODO;
        }
        if (input.startsWith("deadline")) {
            return DEADLINE;
        }
        if (input.startsWith("event")) {
            return EVENT;
        }
        if (input.equalsIgnoreCase("bye")) {
            return EXIT;
        }
        return UNKNOWN;
    }
}
