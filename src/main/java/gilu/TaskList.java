package gilu;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import gilu.exception.GiluException;
import gilu.storage.Storage;
import gilu.task.Deadline;
import gilu.task.Event;
import gilu.task.Task;
import gilu.task.Todo;
import gilu.ui.Ui;

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
        assert tasks != null : "Task list should not be null after initialization";
    }

    /**
     * Constructs a TaskList with preloaded tasks.
     *
     * @param tasks The list of tasks.
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "Provided task list should not be null";
        this.tasks = tasks;
    }

    /**
     * Returns a formatted string of the sorted task list.
     *
     * @param ui The Ui object for formatting messages.
     * @return A formatted string representation of the sorted task list.
     */
    public String getSortedTaskListString(Ui ui) {
        List<Event> sortedEvents = tasks.stream()
                .filter(task -> task instanceof Event)
                .map(task -> (Event) task)
                .sorted(Comparator.comparing(Event::getFrom))
                .collect(Collectors.toList());

        List<Deadline> sortedDeadlines = tasks.stream()
                .filter(task -> task instanceof Deadline)
                .map(task -> (Deadline) task)
                .sorted(Comparator.comparing(Deadline::getBy))
                .collect(Collectors.toList());

        List<Todo> todos = tasks.stream()
                .filter(task -> task instanceof Todo)
                .map(task -> (Todo) task)
                .collect(Collectors.toList());

        StringBuilder response = new StringBuilder();
        response.append(ui.showMessage("Here is your sorted task list:"));

        if (!sortedEvents.isEmpty()) {
            response.append("\nEvents:\n");
            for (int i = 0; i < sortedEvents.size(); i++) {
                response.append("  ").append(i + 1).append(". ").append(sortedEvents.get(i)).append("\n");
            }
        }

        if (!sortedDeadlines.isEmpty()) {
            response.append("\nDeadlines:\n");
            for (int i = 0; i < sortedDeadlines.size(); i++) {
                response.append("  ").append(i + 1).append(". ").append(sortedDeadlines.get(i)).append("\n");
            }
        }

        if (!todos.isEmpty()) {
            response.append("\nTodos:\n");
            for (int i = 0; i < todos.size(); i++) {
                response.append("  ").append(i + 1).append(". ").append(todos.get(i)).append("\n");
            }
        }

        return response.toString();
    }

    /**
     * Returns a formatted string of the task list.
     *
     * @param ui The Ui object for formatting messages.
     * @return A formatted string representation of the task list.
     */
    public String getTaskListString(Ui ui) {
        assert ui != null : "UI object should not be null";
        if (tasks.isEmpty()) {
            return ui.showMessage("Yay! There are no tasks as of now!");
        }
        StringBuilder response = new StringBuilder(ui.showMessage("Here are the tasks in your list:\n"));
        for (int i = 0; i < tasks.size(); i++) {
            response.append("  ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return response.toString();
    }

    /**
     * Returns tasks that occur on a specified date.
     *
     * @param input The user input containing the date in yyyy-MM-dd format.
     * @param ui    The Ui object for formatting messages.
     * @return A formatted string of tasks occurring on the given date.
     * @throws GiluException If the date format is invalid.
     */
    public String listTasksOnDate(String input, Ui ui) throws GiluException {
        assert input != null && !input.isEmpty() : "Input should not be null or empty";
        assert ui != null : "UI object should not be null";
        try {
            String[] parts = input.split(" ");
            LocalDate date = LocalDate.parse(parts[1]);

            StringBuilder response = new StringBuilder(ui.showMessage("Here are the tasks on "
                    + date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":\n"));
            boolean hasTasks = false;
            for (Task task : tasks) {
                if (task instanceof Deadline && ((Deadline) task).getBy().toLocalDate().equals(date)) {
                    response.append("   ").append(task).append("\n");
                    hasTasks = true;
                } else if (task instanceof Event) {
                    Event event = (Event) task;
                    if (!date.isBefore(event.getFrom().toLocalDate()) && !date.isAfter(event.getTo().toLocalDate())) {
                        response.append("   ").append(task).append("\n");
                        hasTasks = true;
                    }
                }
            }

            if (!hasTasks) {
                return ui.showMessage("No tasks found for this date.");
            }
            return response.toString();
        } catch (Exception e) {
            throw new GiluException("Invalid date format. Use yyyy-MM-dd.");
        }
    }

    /**
     * Adds a new todo task and returns the confirmation message.
     *
     * @param input The full user input string, including the command keyword.
     * @param ui    The Ui object.
     * @param storage The Storage object.
     * @return The confirmation message.
     * @throws GiluException If the description is empty.
     */
    public String addTodo(String input, Ui ui, Storage storage) throws GiluException {
        assert input != null : "Input should not be null";
        assert ui != null : "UI object should not be null";
        assert storage != null : "Storage object should not be null";

        // Remove "todo" from the input and trim extra spaces
        String description = input.replaceFirst("(?i)^todo\\s*", "").trim();

        if (description.isEmpty()) { // Ensure that we do not allow empty descriptions
            throw new GiluException("Oops! I need some details for your ToDo.");
        }

        try {
            Task task = new Todo(description);
            tasks.add(task);
            storage.saveTasks(tasks);
            return ui.printAddedTask(task, tasks.size());
        } catch (IOException e) {
            throw new GiluException("Error saving task: " + e.getMessage());
        }
    }

    /**
     * Adds a new deadline task.
     */
    public String addDeadline(String input, Ui ui, Storage storage) throws GiluException {
        assert input != null && !input.isEmpty() : "Input should not be null or empty";
        assert ui != null : "UI object should not be null";
        assert storage != null : "Storage object should not be null";

        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new GiluException("Your deadline is missing something. Try: deadline <task> /by <yyyy-MM-dd HHmm>.");
        }

        try {
            LocalDateTime by = LocalDateTime.parse(parts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            Task task = new Deadline(parts[0].trim(), by);
            tasks.add(task);
            storage.saveTasks(tasks);
            return ui.printAddedTask(task, tasks.size());
        } catch (Exception e) {
            throw new GiluException("Invalid date format! Use: yyyy-MM-dd HHmm.");
        }
    }

    /**
     * Adds a new event task.
     */
    public String addEvent(String input, Ui ui, Storage storage) throws GiluException {
        assert input != null && !input.isEmpty() : "Input should not be null or empty";
        assert ui != null : "UI object should not be null";
        assert storage != null : "Storage object should not be null";

        String[] parts = input.split(" /from ", 2);
        if (parts.length < 2) {
            throw new GiluException("Your event needs details! Use: "
                    + "event <task> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        String[] timeParts = parts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            throw new GiluException("Your event needs both a start and end time.");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(timeParts[0].trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            LocalDateTime to = LocalDateTime.parse(timeParts[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            Task task = new Event(parts[0].trim(), from, to);
            tasks.add(task);
            storage.saveTasks(tasks);
            return ui.printAddedTask(task, tasks.size());
        } catch (Exception e) {
            throw new GiluException("Invalid date format! Use: yyyy-MM-dd HHmm.");
        }
    }

    /**
     * Marks a task as done.
     *
     * @param input   The user input specifying the task to mark.
     * @param ui      The Ui object.
     * @param storage The Storage object.
     * @return The confirmation message.
     * @throws GiluException If the task number is invalid.
     */
    public String markTask(String input, Ui ui, Storage storage) throws GiluException {
        assert input != null && !input.isEmpty() : "Input should not be null or empty";
        assert ui != null : "UI object should not be null";
        assert storage != null : "Storage object should not be null";

        int taskIndex = getValidatedTaskIndex(input);
        tasks.get(taskIndex).markAsDone();
        saveTasks(storage);
        return ui.showMessage("Cool! I've marked this task as done:\n   " + tasks.get(taskIndex));
    }

    /**
     * Unmarks a task.
     *
     * @param input   The user input specifying the task to unmark.
     * @param ui      The Ui object.
     * @param storage The Storage object.
     * @return The confirmation message.
     * @throws GiluException If the task number is invalid.
     */
    public String unmarkTask(String input, Ui ui, Storage storage) throws GiluException {
        assert input != null && !input.isEmpty() : "Input should not be null or empty";
        assert ui != null : "UI object should not be null";
        assert storage != null : "Storage object should not be null";

        int taskIndex = getValidatedTaskIndex(input);
        tasks.get(taskIndex).markAsNotDone();
        saveTasks(storage);
        return ui.showMessage("No problem! I've marked this task as not done:\n   " + tasks.get(taskIndex));
    }

    /**
     * Deletes a task.
     *
     * @param input   The user input specifying the task to delete.
     * @param ui      The Ui object.
     * @param storage The Storage object.
     * @return The confirmation message.
     * @throws GiluException If the task number is invalid.
     */
    public String deleteTask(String input, Ui ui, Storage storage) throws GiluException {
        assert input != null && !input.isEmpty() : "Input should not be null or empty";
        assert ui != null : "UI object should not be null";
        assert storage != null : "Storage object should not be null";

        int taskIndex = getValidatedTaskIndex(input);
        Task removedTask = tasks.remove(taskIndex);
        saveTasks(storage);
        return ui.showMessage("Noted. I've removed this task:\n   " + removedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Finds tasks by keyword (case-insensitive and partial match).
     *
     * @param input The full user input containing the command and keyword.
     * @param ui    The Ui object for displaying results.
     * @return The formatted string of matching tasks.
     */
    public String findTasks(String input, Ui ui) {
        assert input != null && !input.isEmpty() : "Search input should not be null or empty";
        assert ui != null : "UI object should not be null";

        // Extract keyword from input
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            return ui.showMessage("Oops! Please specify a keyword to search.");
        }

        String keyword = parts[1].trim().toLowerCase(); // Convert to lowercase

        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword)) { // Case-insensitive match
                matchingTasks.add(task);
            }
        }

        if (matchingTasks.isEmpty()) {
            return ui.showMessage("No matching tasks found.");
        }

        StringBuilder response = new StringBuilder(ui.showMessage("Here are the matching tasks:\n"));
        for (int i = 0; i < matchingTasks.size(); i++) {
            response.append("  ").append(i + 1).append(". ").append(matchingTasks.get(i)).append("\n");
        }
        return response.toString();
    }

    /**
     * Saves the current list of tasks to storage.
     *
     * <p>This method ensures that all tasks are persisted in the specified
     * storage. If an {@link IOException} occurs during the saving process,
     * an error message is printed to the console.</p>
     *
     * @param storage The {@link Storage} object used to save tasks.
     * @throws AssertionError If the provided storage object is {@code null}.
     */
    private void saveTasks(Storage storage) {
        assert storage != null : "Storage object should not be null";

        try {
            storage.saveTasks(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Extracts and validates the task index from user input.
     *
     * @param input The user input containing the command and number.
     * @return The valid task index (0-based).
     * @throws GiluException If input is invalid or number is out of range.
     */
    private int getValidatedTaskIndex(String input) throws GiluException {
        assert input != null && !input.isEmpty() : "Input should not be null or empty";

        // Ensure input format: command followed by a single number
        String[] parts = input.trim().split(" ");
        if (parts.length != 2 || !parts[1].matches("\\d+")) {
            throw new GiluException("Oops! Please provide a valid task number.");
        }

        int taskIndex = Integer.parseInt(parts[1]) - 1;

        // Validate task index range
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new GiluException("Hmm, I can’t find that task. Are you sure it’s on the list?");
        }

        return taskIndex;
    }

    /**
     * Returns the number of tasks in the task list.
     *
     * @return The number of tasks.
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable view of the tasks list.
     *
     * @return A list of tasks.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
}
