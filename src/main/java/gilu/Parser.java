package gilu;

import gilu.command.Command;
import gilu.exception.GiluException;

/**
 * Parses user input and executes the corresponding commands.
 */
public class Parser {

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
    public String executeCommand(String input, TaskList tasks, Ui ui, Storage storage) throws GiluException {
        Command command = Command.fromInput(input);

        switch (command) {
            case LIST:
                return tasks.getTaskListString(ui);
            case LIST_DATE:
                return tasks.listTasksOnDate(input, ui);
            case TODO:
                return tasks.addTodo(input.substring(4).trim(), ui, storage);
            case DEADLINE:
                return tasks.addDeadline(input.substring(8).trim(), ui, storage);
            case EVENT:
                return tasks.addEvent(input.substring(5).trim(), ui, storage);
            case MARK:
                return tasks.markTask(input, ui, storage);
            case UNMARK:
                return tasks.unmarkTask(input, ui, storage);
            case DELETE:
                return tasks.deleteTask(input, ui, storage);
            case FIND:
                if (input.length() <= 5) {
                    throw new GiluException("Oops! Please specify a keyword to search.");
                }
                return tasks.findTasks(input.substring(5).trim(), ui);
            case EXIT:
                return ui.showMessage("Bye for now! But I hope to see you again soon!");
            default:
                return ui.showMessage("Uh-oh! I didn’t get that. Try 'list', " +
                        "'todo', 'deadline', 'event', 'mark', 'unmark', 'find' or 'delete'.");
        }
    }
}
