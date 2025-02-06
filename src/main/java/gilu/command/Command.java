package gilu.command;

/**
 * Represents the different commands supported by Gilu.
 */
public enum Command {
    LIST, LIST_DATE, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, EXIT, UNKNOWN;

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
        if (input.matches("list \\d{4}-\\d{2}-\\d{2}")) {
            return LIST_DATE;
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
        if (input.startsWith("find")) {
            return FIND;
        }
        if (input.equalsIgnoreCase("bye")) {
            return EXIT;
        }
        return UNKNOWN;
    }
}
