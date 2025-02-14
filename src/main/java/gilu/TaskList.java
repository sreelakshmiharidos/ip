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
     * Returns a formatted string of the task list.
     *
     * @param ui The Ui object for formatting messages.
     * @return A formatted string representation of the task list.
     */
    public String getTaskListString(Ui ui) {
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
        try {
            String[] parts = input.split(" ");
            LocalDate date = LocalDate.parse(parts[1]);

            StringBuilder response = new StringBuilder(ui.showMessage("Here are the tasks on " +
                    date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":\n"));

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
     * @param description The task description.
     * @param ui          The Ui object.
     * @param storage     The Storage object.
     * @return The confirmation message.
     * @throws GiluException If the description is empty.
     */
    public String addTodo(String description, Ui ui, Storage storage) throws GiluException {
        if (description.isEmpty()) {
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
        String[] parts = input.split(" /from ", 2);
        if (parts.length < 2) {
            throw new GiluException("Your event needs details! Use: event <task> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        String[] timeParts = parts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            throw new GiluException("Your event needs both a start and end time.");
        }

        try {
            LocalDateTime from = LocalDateTime.parse(timeParts[0].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
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
     */
    public String markTask(String input, Ui ui, Storage storage) throws GiluException {
        int taskIndex = getTaskIndex(input);
        tasks.get(taskIndex).markAsDone();
        saveTasks(storage);
        return ui.showMessage("Cool! I've marked this task as done:\n   " + tasks.get(taskIndex));
    }

    /**
     * Unmarks a task.
     */
    public String unmarkTask(String input, Ui ui, Storage storage) throws GiluException {
        int taskIndex = getTaskIndex(input);
        tasks.get(taskIndex).markAsNotDone();
        saveTasks(storage);
        return ui.showMessage("No problem! I've marked this task as not done:\n   " + tasks.get(taskIndex));
    }

    /**
     * Deletes a task.
     */
    public String deleteTask(String input, Ui ui, Storage storage) throws GiluException {
        int taskIndex = getTaskIndex(input);
        Task removedTask = tasks.remove(taskIndex);
        saveTasks(storage);
        return ui.showMessage("Noted. I've removed this task:\n   " + removedTask +
                "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Finds tasks by keyword.
     */
    public String findTasks(String keyword, Ui ui) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
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

    private void saveTasks(Storage storage) {
        try {
            storage.saveTasks(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private int getTaskIndex(String input) throws GiluException {
        return Integer.parseInt(input.split(" ")[1]) - 1;
    }
}
