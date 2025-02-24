package gilu.command;

import java.util.regex.Pattern;

/**
 * Represents the different commands supported by Gilu.
 */
public enum Command {
    LIST, SORT, LIST_DATE, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND, EXIT, UNKNOWN;

    /**
     * Regular expression to match date format YYYY-MM-DD.
     */
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

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
                // If a second word is present and is a valid date, classify as LIST_DATE
                if (words.length > 1 && DATE_PATTERN.matcher(words[1]).matches()) {
                    return LIST_DATE;
                }
                return LIST;
            case "sort":
                return SORT;
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
                return UNKNOWN;
        }
    }
}

