package gilu;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            case LIST_DATE:
                listTasksOnDate(input);
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
                addTodo(input.substring(4).trim());
                saveTasks();
                break;
            case DEADLINE:
                addDeadline(input.substring(8).trim());
                saveTasks();
                break;
            case EVENT:
                addEvent(input.substring(5).trim());
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
     * Lists tasks that occur on a specified date or datetime.
     *
     * @param input The user input containing the date in the format yyyy-MM-dd or yyyy-MM-dd HH:mm.
     * @throws GiluException If the date format is invalid or no tasks match the input.
     */
    private static void listTasksOnDate(String input) throws GiluException {
        try {
            String[] parts = input.split(" ");
            LocalDate date;

            // Check if input has only date (yyyy-MM-dd) or date-time (yyyy-MM-dd HH:mm)
            if (parts[1].matches("\\d{4}-\\d{2}-\\d{2}")) {
                date = LocalDate.parse(parts[1]); // Parse date-only
            } else {
                throw new GiluException("Invalid date format. Use yyyy-MM-dd.");
            }

            printLine();
            System.out.println(" Here are the tasks on " + date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");

            boolean hasTasks = false;
            for (Task task : tasks) {
                if (task instanceof Deadline && ((Deadline) task).getBy().toLocalDate().equals(date)) {
                    System.out.println("   " + task);
                    hasTasks = true;
                } else if (task instanceof Event) {
                    Event event = (Event) task;
                    if (!date.isBefore(event.getFrom().toLocalDate()) && !date.isAfter(event.getTo().toLocalDate())) {
                        System.out.println("   " + task);
                        hasTasks = true;
                    }
                }
            }

            if (!hasTasks) {
                System.out.println("   No tasks found for this date.");
            }
            printLine();
        } catch (Exception e) {
            throw new GiluException("Invalid date format. Use yyyy-MM-dd.");
        }
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
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new GiluException("Whoops! Your deadline is missing something. Try: deadline <task> /by <yyyy-MM-dd HHmm>.");
        }

        try {
            LocalDateTime by = LocalDateTime.parse(parts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            tasks.add(new Deadline(parts[0].trim(), by));
            printAddedTask();
        } catch (Exception e) {
            throw new GiluException("Invalid date format! Use: yyyy-MM-dd HHmm (e.g., 2023-12-15 1800)");
        }
    }

    /**
     * Adds a new Event task.
     *
     * @param input The task description, start time, and end time.
     * @throws GiluException If the input format is incorrect.
     */
    private static void addEvent(String input) throws GiluException {
        String[] parts = input.split(" /from ", 2);
        if (parts.length < 2) {
            throw new GiluException("Your event needs details! Use: event <task> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        String[] timeParts = parts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            throw new GiluException("Oops! Your event needs both a start and end time. Try: event <task> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(timeParts[0].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            LocalDateTime to = LocalDateTime.parse(timeParts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            tasks.add(new Event(parts[0].trim(), from, to));
            printAddedTask();
        } catch (Exception e) {
            throw new GiluException("Invalid date format! Use: yyyy-MM-dd HHmm (e.g., 2023-12-10 1400)");
        }
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
