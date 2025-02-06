package gilu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gilu is a chatbot that helps manage tasks.
 * Tasks are stored persistently using file-based storage.
 */
public class Gilu {
    private static final String DEFAULT_STORAGE_PATH = "./data/gilu.txt";
    private static List<Task> tasks;
    private static Storage storage;

    /**
     * The main entry point of the chatbot.
     *
     * @param args Optional arguments (e.g., custom storage file path for testing).
     */
    public static void main(String[] args) {
        // Determine the storage file path (real or test file)
        String storagePath = (args.length > 0) ? args[0] : DEFAULT_STORAGE_PATH;
        storage = new Storage(storagePath);

        // Load tasks from file
        try {
            tasks = storage.loadTasks();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<>();
        }

        // Welcome message
        printLine();
        System.out.println(" Heyoo! I'm Gilu, your trusted friend!");
        System.out.println(" How can I make your day better?");
        printLine();

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            String input = scanner.nextLine().trim();
            try {
                isRunning = processCommand(input);
            } catch (GiluException e) {
                printLine();
                System.out.println(e.getMessage());
                printLine();
            }
        }

        scanner.close();
    }

    /**
     * Processes a user command and performs the corresponding action.
     *
     * @param input The user input command.
     * @return True if the chatbot should continue running, false if it should exit.
     * @throws GiluException If the command is invalid.
     */
    private static boolean processCommand(String input) throws GiluException {
        Command command = Command.fromInput(input);

        switch (command) {
            case LIST:
                printTasks();
                break;
            case MARK:
                markTask(input);
                saveTasks();
                break;
            case UNMARK:
                unmarkTask(input);
                saveTasks();
                break;
            case DELETE:
                deleteTask(input);
                saveTasks();
                break;
            case TODO:
                addTodo(input.length() > 4 ? input.substring(4).trim() : "");
                saveTasks();
                break;
            case DEADLINE:
                addDeadline(input.length() > 8 ? input.substring(8).trim() : "");
                saveTasks();
                break;
            case EVENT:
                addEvent(input.length() > 5 ? input.substring(5).trim() : "");
                saveTasks();
                break;
            case EXIT:
                printLine();
                System.out.println(" Bye for now! But I hope to see you again soon!");
                printLine();
                return false;
            default:
                throw new GiluException("Uh-oh! I didn’t get that. Try 'list', 'todo', 'deadline', 'event', or 'delete'.");
        }

        return true;
    }

    /**
     * Prints a horizontal line for UI formatting.
     */
    private static void printLine() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * Displays the list of current tasks.
     */
    private static void printTasks() {
        printLine();
        if (tasks.isEmpty()) {
            System.out.println(" Yay! There are no tasks as of now!");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + ". " + tasks.get(i));
            }
        }
        printLine();
    }

    /**
     * Retrieves the task index from the user input.
     *
     * @param input The user command containing the task number.
     * @return The index of the task in the list.
     * @throws GiluException If the input is invalid.
     */
    private static int getTaskIndex(String input) throws GiluException {
        try {
            int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new GiluException("Hmm, I can’t find that task. Are you sure it’s on the list?");
            }
            return taskIndex;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new GiluException("Use: <command> <task_number>. Let’s try again!");
        }
    }

    /**
     * Marks a task as done.
     *
     * @param input The user input specifying the task to mark.
     * @throws GiluException If the task number is invalid.
     */
    private static void markTask(String input) throws GiluException {
        int taskIndex = getTaskIndex(input);
        tasks.get(taskIndex).markAsDone();
        printLine();
        System.out.println(" Cool! I've marked this task as done:");
        System.out.println("   " + tasks.get(taskIndex));
        printLine();
    }

    /**
     * Unmarks a task.
     *
     * @param input The user input specifying the task to unmark.
     * @throws GiluException If the task number is invalid.
     */
    private static void unmarkTask(String input) throws GiluException {
        int taskIndex = getTaskIndex(input);
        tasks.get(taskIndex).markAsNotDone();
        printLine();
        System.out.println(" No problem! I've marked this task as not done yet:");
        System.out.println("   " + tasks.get(taskIndex));
        printLine();
    }

    /**
     * Deletes a task from the list.
     *
     * @param input The user input specifying the task to delete.
     * @throws GiluException If the task number is invalid.
     */
    private static void deleteTask(String input) throws GiluException {
        int taskIndex = getTaskIndex(input);
        Task removedTask = tasks.remove(taskIndex);
        printLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
    }

    /**
     * Adds a new Todo task.
     *
     * @param description The description of the todo task.
     * @throws GiluException If the description is empty.
     */
    private static void addTodo(String description) throws GiluException {
        if (description.isEmpty()) {
            throw new GiluException("Oops! I need some details for your ToDo. What should I remind you about?");
        }
        tasks.add(new Todo(description));
        printAddedTask();
    }

    /**
     * Adds a new Deadline task.
     *
     * @param input The task description and deadline.
     * @throws GiluException If the input format is incorrect.
     */
    private static void addDeadline(String input) throws GiluException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2) {
            throw new GiluException("Whoops! Your deadline is missing something. Try: deadline <task> /by <date>.");
        }
        tasks.add(new Deadline(parts[0], parts[1]));
        printAddedTask();
    }

    /**
     * Adds a new Event task.
     *
     * @param input The task description, start time, and end time.
     * @throws GiluException If the input format is incorrect.
     */
    private static void addEvent(String input) throws GiluException {
        String[] parts = input.split(" /from | /to ", 3);
        if (parts.length < 3) {
            throw new GiluException("Your event needs details! Use: event <task> /from <start> /to <end>");
        }
        tasks.add(new Event(parts[0], parts[1], parts[2]));
        printAddedTask();
    }

    /**
     * Saves the current task list to storage.
     */
    private static void saveTasks() {
        try {
            storage.saveTasks(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Prints task count after adding a task.
     */
    private static void printAddedTask() {
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks.get(tasks.size() - 1));
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
    }
}
