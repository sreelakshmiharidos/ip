package gilu;

import gilu.command.Command;
import gilu.exception.GiluException;

/**
 * Parses user input and executes the corresponding commands.
 */
public class Parser {

    /**
     * Executes a command based on user input.
     *
     * @param input    The user input command.
     * @param tasks    The TaskList object managing tasks.
     * @param ui       The Ui object handling user interactions.
     * @param storage  The Storage object for saving/loading tasks.
     * @return True if the chatbot should continue running, false if it should exit.
     * @throws GiluException If the command is invalid or an error occurs.
     */
    public boolean executeCommand(String input, TaskList tasks, Ui ui, Storage storage) throws GiluException {
        Command command = Command.fromInput(input);

        switch (command) {
            case LIST:
                tasks.printTasks(ui);
                break;
            case LIST_DATE:
                tasks.listTasksOnDate(input, ui);
                break;
            case TODO:
                tasks.addTodo(input.substring(4).trim(), ui, storage);
                break;
            case DEADLINE:
                tasks.addDeadline(input.substring(8).trim(), ui, storage);
                break;
            case EVENT:
                tasks.addEvent(input.substring(5).trim(), ui, storage);
                break;
            case MARK:
                tasks.markTask(input, ui, storage);
                break;
            case UNMARK:
                tasks.unmarkTask(input, ui, storage);
                break;
            case DELETE:
                tasks.deleteTask(input, ui, storage);
                break;
            case FIND:
                if (input.length() <= 5) {
                    throw new GiluException("Oops! Please specify a keyword to search.");
                }
                tasks.findTasks(input.substring(5).trim(), ui);
                break;
            case EXIT:
                ui.showMessage(" Bye for now! But I hope to see you again soon!");
                return false;
            default:
                throw new GiluException("Uh-oh! I didn’t get that. Try 'list', 'todo', 'deadline', 'event', or 'delete'.");
        }

        return true;
    }
}
