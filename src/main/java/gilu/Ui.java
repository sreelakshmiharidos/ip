package gilu;

import gilu.task.Task;

/**
 * Handles user interactions.
 */
public class Ui {

    public String showMessage(String message) {
        return message + "\n";
    }

    public String showWelcomeMessage() {
        return " Heyoo! I'm Gilu, your trusted task-manager!\n" +
                " How can I make your day better?\n";
    }

    public String printAddedTask(Task task, int taskCount) {
        return showMessage("Got it. I've added this task:\n   " + task + "\nNow you have " + taskCount + " tasks.");
    }
}
