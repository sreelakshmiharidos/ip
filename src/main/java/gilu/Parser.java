package gilu;

import gilu.command.Command;
import gilu.exception.GiluException;

/**
 * Parses user input and executes the corresponding commands.
 */
public class Parser {
    private static final String ERROR_UNKNOWN_COMMAND = "Uh-oh! I didn’t get that. "
            + "Try 'list', 'todo', 'deadline', 'event', 'mark', 'unmark', 'find', 'delete' or 'sort'.";

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

        switch (command) {
            case LIST:
                return tasks.getTaskListString(ui);

            case SORT:
                return tasks.getSortedTaskListString(ui);

            case LIST_DATE:
                return tasks.listTasksOnDate(input, ui);

            case TODO:
                return tasks.addTodo(input, ui, storage);

            case DEADLINE:
                return tasks.addDeadline(input, ui, storage);

            case EVENT:
                return tasks.addEvent(input, ui, storage);

            case MARK:
                return tasks.markTask(input, ui, storage);

            case UNMARK:
                return tasks.unmarkTask(input, ui, storage);

            case DELETE:
                return tasks.deleteTask(input, ui, storage);

            case FIND:
                if (input.split(" ").length < 2) { // Ensure keyword exists
                    throw new GiluException(ERROR_MISSING_KEYWORD);
                }
                return tasks.findTasks(input, ui);

            case EXIT:
                return ui.showMessage(GOODBYE_MESSAGE);

            default:
                return ui.showMessage(ERROR_UNKNOWN_COMMAND);
        }
    }
}
