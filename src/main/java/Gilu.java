import java.util.Scanner;

public class Gilu {
    private static final int MAX_TASKS = 100;
    private static Task[] tasks = new Task[MAX_TASKS]; // Array of Task objects
    private static int taskCount = 0;

    public static void main(String[] args) {
        // Welcome message
        printLine();
        System.out.println(" Heyoo! I'm Gilu, your trusted friend!");
        System.out.println(" How can I make your day better?");
        printLine();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                // Exit message
                printLine();
                System.out.println(" Bye for now! But I hope to see you again soon!");
                printLine();
                break;
            } else if (input.equalsIgnoreCase("list")) {
                printTasks(); // Display tasks
            } else if (input.startsWith("mark ")) {
                markTask(input);
            } else if (input.startsWith("unmark ")) {
                unmarkTask(input);
            } else {
                addTask(input); // Store new task
            }
        }

        scanner.close();
    }

    // Prints horizontal squiggly lines
    private static void printLine() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    // Adds a new task
    private static void addTask(String taskDescription) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new Task(taskDescription);
            taskCount++;
            printLine();
            System.out.println(" Added: " + taskDescription);
            printLine();
        } else {
            printLine();
            System.out.println(" Sorry, I can't store more tasks! Limit reached.");
            printLine();
        }
    }

    // Displays the task list
    private static void printTasks() {
        printLine();
        if (taskCount == 0) {
            System.out.println(" No tasks stored yet.");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < taskCount; i++) {
                System.out.println(" " + (i + 1) + ". " + tasks[i]);
            }
        }
        printLine();
    }

    // Marks a task as done
    private static void markTask(String input) {
        try {
            int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
            if (taskIndex >= 0 && taskIndex < taskCount) {
                tasks[taskIndex].markAsDone();
                printLine();
                System.out.println(" Cool! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);
                printLine();
            } else {
                printLine();
                System.out.println(" Invalid task number.");
                printLine();
            }
        } catch (Exception e) {
            printLine();
            System.out.println(" Invalid command format. Use: mark <task_number>");
            printLine();
        }
    }

    // Unmarks a task (sets it back to not done)
    private static void unmarkTask(String input) {
        try {
            int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
            if (taskIndex >= 0 && taskIndex < taskCount) {
                tasks[taskIndex].markAsNotDone();
                printLine();
                System.out.println(" No problem! I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
                printLine();
            } else {
                printLine();
                System.out.println(" Invalid task number.");
                printLine();
            }
        } catch (Exception e) {
            printLine();
            System.out.println(" Invalid command format. Use: unmark <task_number>");
            printLine();
        }
    }
}
