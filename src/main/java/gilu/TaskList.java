package gilu;

import gilu.exception.GiluException;
import gilu.task.Deadline;
import gilu.task.Event;
import gilu.task.Task;
import gilu.task.Todo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the list of tasks.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with preloaded tasks.
     *
     * @param tasks The list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Prints the list of tasks.
     *
     * @param ui The Ui object for user interaction.
     */
    public void printTasks(Ui ui) {
        if (tasks.isEmpty()) {
            ui.showMessage(" Yay! There are no tasks as of now!");
        } else {
            ui.showLine();
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + ". " + tasks.get(i));
            }
            ui.showLine();
        }
    }

    /**
     * Lists tasks that occur on a specified date.
     *
     * @param input The user input containing the date in yyyy-MM-dd format.
     * @param ui The Ui object for user interaction.
     * @throws GiluException If the date format is invalid.
     */
    public void listTasksOnDate(String input, Ui ui) throws GiluException {
        try {
            String[] parts = input.split(" ");
            LocalDate date = LocalDate.parse(parts[1]);

            ui.showLine();
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
            ui.showLine();
        } catch (Exception e) {
            throw new GiluException("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    /**
     * Adds a new todo task.
     *
     * @param description The task description.
     * @param ui The Ui object.
     * @param storage The Storage object.
     * @throws GiluException If the description is empty.
     */
    public void addTodo(String description, Ui ui, Storage storage) throws GiluException {
        if (description.isEmpty()) {
            throw new GiluException("Oops! I need some details for your ToDo. What should I remind you about?");
        }

        try {
            addTask(new Todo(description), ui, storage);
        } catch (IOException e) {
            throw new GiluException("Error saving task: " + e.getMessage());
        }
    }

    /**
     * Adds a new deadline task.
     *
     * @param input The task description and deadline.
     * @param ui The Ui object.
     * @param storage The Storage object.
     * @throws GiluException If the input format is incorrect.
     */
    public void addDeadline(String input, Ui ui, Storage storage) throws GiluException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new GiluException("Whoops! Your deadline is missing something. Try: deadline <task> /by <yyyy-MM-dd HHmm>.");
        }

        try {
            LocalDateTime by = LocalDateTime.parse(parts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            addTask(new Deadline(parts[0].trim(), by), ui, storage);
        } catch (Exception e) {
            throw new GiluException("Invalid date format! Use: yyyy-MM-dd HHmm (e.g., 2023-12-15 1800)");
        }
    }

    /**
     * Adds a new event task.
     *
     * @param input The task description, start time, and end time.
     * @param ui The Ui object.
     * @param storage The Storage object.
     * @throws GiluException If the input format is incorrect.
     */
    public void addEvent(String input, Ui ui, Storage storage) throws GiluException {
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
            addTask(new Event(parts[0].trim(), from, to), ui, storage);
        } catch (Exception e) {
            throw new GiluException("Invalid date format! Use: yyyy-MM-dd HHmm (e.g., 2023-12-10 1400)");
        }
    }

    /**
     * Marks a task as done.
     *
     * @param input The user input specifying the task to mark.
     * @param ui The Ui object.
     * @param storage The Storage object.
     * @throws GiluException If the task number is invalid.
     */
    public void markTask(String input, Ui ui, Storage storage) throws GiluException {
        int taskIndex = getTaskIndex(input);
        tasks.get(taskIndex).markAsDone();
        ui.showMessage(" Cool! I've marked this task as done:\n   " + tasks.get(taskIndex));
        saveTasks(storage);
    }

    /**
     * Unmarks a task as not done.
     *
     * @param input   The user input containing the task number.
     * @param ui      The UI instance for displaying messages.
     * @param storage The Storage instance for saving the updated task list.
     * @throws GiluException If the task number is invalid.
     */
    public void unmarkTask(String input, Ui ui, Storage storage) throws GiluException {
        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new GiluException("Hmm, I can’t find that task. Are you sure it’s on the list?");
            }

            Task task = tasks.get(index);
            task.markAsNotDone();

            // Show success message in one call
            ui.showMessage(" No problem! I've marked this task as not done yet:\n   " + task);

            // Save updated task list
            storage.saveTasks(tasks);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new GiluException("Use: unmark <task_number>. Let’s try again!");
        } catch (IOException e) {
            throw new GiluException("Error saving task list after unmarking: " + e.getMessage());
        }
    }

    /**
     * Deletes a task from the list.
     *
     * @param input The user input specifying the task to delete.
     * @param ui The Ui object.
     * @param storage The Storage object.
     * @throws GiluException If the task number is invalid.
     */
    public void deleteTask(String input, Ui ui, Storage storage) throws GiluException {
        int taskIndex = getTaskIndex(input);
        Task removedTask = tasks.remove(taskIndex);

        // Show removal message along with remaining tasks count
        ui.showMessage(" Noted. I've removed this task:\n   " + removedTask +
                "\n Now you have " + tasks.size() + " tasks in the list.");

        saveTasks(storage);
    }

    /**
     * Saves tasks to storage.
     *
     * @param storage The Storage object handling file persistence.
     */
    private void saveTasks(Storage storage) {
        try {
            storage.saveTasks(tasks);
        } catch (Exception e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Retrieves the task index from the user input.
     *
     * @param input The user command containing the task number.
     * @return The index of the task in the list.
     * @throws GiluException If the input is invalid.
     */
    private int getTaskIndex(String input) throws GiluException {
        try {
            int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new GiluException("Hmm, I can’t find that task. Are you sure it’s on the list?");
            }
            return taskIndex;
        } catch (Exception e) {
            throw new GiluException("Use: <command> <task_number>. Let’s try again!");
        }
    }

    /**
     * Adds a task to the task list, displays a confirmation message,
     * and saves the updated list to storage.
     *
     * @param task    The task to be added.
     * @param ui      The user interface to display messages.
     * @param storage The storage handler for saving tasks.
     * @throws IOException If an error occurs while saving the task list.
     */
    public void addTask(Task task, Ui ui, Storage storage) throws IOException {
        tasks.add(task);
        ui.printAddedTask(task, tasks.size());
        storage.saveTasks(tasks);
    }
}
