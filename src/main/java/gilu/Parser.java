package gilu;

import gilu.command.Command;
import gilu.exception.GiluException;

/**
 * Parses user input and executes the corresponding commands.
 */
public class Parser {
    private static final String ERROR_UNKNOWN_COMMAND = "Uh-oh! I didn’t get that. "
            + "Try 'list', 'todo', 'deadline', 'event', 'mark', 'unmark', 'find' or 'delete'.";

    private static final String ERROR_MISSING_KEYWORD = "Oops! Please specify a keyword to search.";

    private static final String GOODBYE_MESSAGE = "Bye for now! But I hope to see you again soon!";

    /**
     * Executes a command based on user input and returns a response string.
     *
     * @param input    The user input command.
     * @param tasks    The TaskList object managing tasks.
     * @param ui       The Ui object handling user interactions.
     * @param storage  The Storage object for saving/loading tasks.
     * @return The response to be displayed in the GUI.
     * @throws GiluException If the command is invalid or an error occurs.
     */
    public String executeCommand(final String input, final TaskList tasks, final Ui ui, final Storage storage)
            throws GiluException {
        Command command = Command.fromInput(input);
        String commandArgs = input.replaceFirst("^\\S+\\s*", ""); // Removes command name dynamically

        switch (command) {
            case LIST:
                return tasks.getTaskListString(ui);

            case LIST_DATE:
                return tasks.listTasksOnDate(commandArgs, ui);

            case TODO:
                return tasks.addTodo(commandArgs, ui, storage);

            case DEADLINE:
                return tasks.addDeadline(commandArgs, ui, storage);

            case EVENT:
                return tasks.addEvent(commandArgs, ui, storage);

            case MARK:
                return tasks.markTask(commandArgs, ui, storage);

            case UNMARK:
                return tasks.unmarkTask(commandArgs, ui, storage);

            case DELETE:
                return tasks.deleteTask(commandArgs, ui, storage);

            case FIND:
                if (commandArgs.isEmpty()) {
                    throw new GiluException(ERROR_MISSING_KEYWORD);
                }
                return tasks.findTasks(commandArgs, ui);

            case EXIT:
                return ui.showMessage(GOODBYE_MESSAGE);

            default:
                return ui.showMessage(ERROR_UNKNOWN_COMMAND);
        }
    }
}
