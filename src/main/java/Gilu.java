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
                printTasks(); // Display stored tasks
            } else if (input.startsWith("mark ")) {
                markTask(input);
            } else if (input.startsWith("unmark ")) {
                unmarkTask(input);
            } else if (input.startsWith("todo ")) {
                addTodo(input.substring(5));
            } else if (input.startsWith("deadline ")) {
                addDeadline(input.substring(9));
            } else if (input.startsWith("event ")) {
                addEvent(input.substring(6));
            } else {
                printLine();
                System.out.println(" I'm not sure what you mean. Try 'list', 'todo', 'deadline', or 'event'.");
                printLine();
            }
        }

        scanner.close();
    }

    // Prints horizontal squiggly lines
    private static void printLine() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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

    // Adds a Todo task
    private static void addTodo(String description) {
        tasks[taskCount++] = new Todo(description);
        printAddedTask();
    }

    // Adds a Deadline task
    private static void addDeadline(String input) {
        String[] parts = input.split(" /by ", 2);
        if (parts.length == 2) {
            tasks[taskCount++] = new Deadline(parts[0], parts[1]);
            printAddedTask();
        } else {
            printLine();
            System.out.println(" Invalid format. Use: deadline <task> /by <date>");
            printLine();
        }
    }

    // Adds an Event task
    private static void addEvent(String input) {
        String[] parts = input.split(" /from | /to ", 3);
        if (parts.length == 3) {
            tasks[taskCount++] = new Event(parts[0], parts[1], parts[2]);
            printAddedTask();
        } else {
            printLine();
            System.out.println(" Invalid format. Use: event <task> /from <start> /to <end>");
            printLine();
        }
    }

    // Prints task count after adding a task
    private static void printAddedTask() {
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks[taskCount - 1]);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        printLine();
    }
}
