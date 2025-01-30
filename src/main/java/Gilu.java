import java.util.Scanner;

public class Gilu {
    private static final int MAX_TASKS = 100;  // maximum number of tasks
    private static String[] tasks = new String[MAX_TASKS]; // fixed-size array for tasks
    private static int taskCount = 0;  // counter for tasks

    public static void main(String[] args) {
        // welcome message
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
                printTasks();  // displays current task list
            } else {
                addTask(input);  // adds a new task
            }
        }

        scanner.close();
    }

    // helper method to print horizontal squiggly lines
    private static void printLine() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    // method to add a task
    private static void addTask(String task) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = task;
            taskCount++;
            printLine();
            System.out.println(" Added: " + task);
            printLine();
        } else {
            printLine();
            System.out.println(" Sorry, I can't store more tasks! Limit reached.");
            printLine();
        }
    }

    // method to display the task list
    private static void printTasks() {
        printLine();
        if (taskCount == 0) {
            System.out.println(" No tasks stored yet.");
        } else {
            for (int i = 0; i < taskCount; i++) {
                System.out.println(" " + (i + 1) + ". " + tasks[i]);
            }
        }
        printLine();
    }
}
