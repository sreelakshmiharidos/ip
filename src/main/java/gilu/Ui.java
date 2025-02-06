package gilu;

import gilu.task.Task;

/**
 * Handles user interactions.
 */
public class Ui {

    /**
     * Prints a horizontal line.
     */
    public void showLine() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * Displays a message to the user.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Displays the welcome message.
     */
    public void showWelcomeMessage() {
        showLine();
        System.out.println(" Heyoo! I'm Gilu, your trusted friend!");
        System.out.println(" How can I make your day better?");
        showLine();
    }

    /**
     * Prints a confirmation message for adding a task.
     *
     * @param task      The task that was added.
     * @param taskCount The total number of tasks after addition.
     */
    public void printAddedTask(Task task, int taskCount) {
        showMessage(" Got it. I've added this task:\n   " + task + "\n Now you have "
                + taskCount + (taskCount == 1 ? " task" : " tasks") + " in the list.");
    }
}
