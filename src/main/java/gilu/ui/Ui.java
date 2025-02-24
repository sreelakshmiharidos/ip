package gilu.ui;

import gilu.task.Task;

/**
 * Handles user interactions.
 */
public class Ui {

    /**
     * Returns a formatted message.
     *
     * @param message The message to display.
     * @return The formatted message.
     */
    public String showMessage(String message) {
        return message + "\n";
    }

    /**
     * Returns a confirmation message when a task is added.
     *
     * @param task The task that was added.
     * @param taskCount The total number of tasks after addition.
     * @return The formatted confirmation message.
     */
    public String printAddedTask(Task task, int taskCount) {
        return showMessage("Got it. I've added this task:\n   " + task + "\nNow you have " + taskCount + " tasks.");
    }
}
