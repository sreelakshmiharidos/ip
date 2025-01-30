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
            try {
                if (input.equalsIgnoreCase("bye")) {
                    // Exit message
                    printLine();
                    System.out.println(" Bye for now! But I hope to see you again soon!");
                    printLine();
                    break;
                }
                processCommand(input);
            } catch (GiluException e) {
                printLine();
                System.out.println(e.getMessage());
                printLine();
            }
        }

        scanner.close();
    }

    // Processes the user's command and throws GiluException for errors
    private static void processCommand(String input) throws GiluException {
        if (input.equalsIgnoreCase("list")) {
            printTasks();
        } else if (input.startsWith("mark")) {
            markTask(input);
        } else if (input.startsWith("unmark")) {
            unmarkTask(input);
        } else if (input.startsWith("todo")) {
            addTodo(input.length() > 4 ? input.substring(4).trim() : "");
        } else if (input.startsWith("deadline")) {
            addDeadline(input.length() > 8 ? input.substring(8).trim() : "");
        } else if (input.startsWith("event")) {
            addEvent(input.length() > 5 ? input.substring(5).trim() : "");
        } else {
            throw new GiluException("Uh-oh! I didn’t get that. Try 'list', 'todo', 'deadline', or 'event' instead!");
        }
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
    private static void markTask(String input) throws GiluException {
        try {
            String[] parts = input.split(" ");
            if (parts.length < 2) {
                throw new GiluException("Oops! You need to specify a task number. Use: mark <task_number>");
            }
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            if (taskIndex >= 0 && taskIndex < taskCount) {
                tasks[taskIndex].markAsDone();
                printLine();
                System.out.println(" Cool! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);
                printLine();
            } else {
                throw new GiluException("Hmm, I can’t find that task. Are you sure it’s on the list?");
            }
        } catch (NumberFormatException e) {
            throw new GiluException("Use: mark <task_number>. Let’s try again!");
        }
    }

    // Unmarks a task
    private static void unmarkTask(String input) throws GiluException {
        try {
            String[] parts = input.split(" ");
            if (parts.length < 2) {
                throw new GiluException("Oops! You need to specify a task number. Use: unmark <task_number>");
            }
            int taskIndex = Integer.parseInt(parts[1]) - 1;
            if (taskIndex >= 0 && taskIndex < taskCount) {
                tasks[taskIndex].markAsNotDone();
                printLine();
                System.out.println(" No problem! I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
                printLine();
            } else {
                throw new GiluException("Hmm, I can’t find that task. Are you sure it’s on the list?");
            }
        } catch (NumberFormatException e) {
            throw new GiluException("Use: unmark <task_number>. Let’s try again!");
        }
    }

    // Adds a Todo task
    private static void addTodo(String description) throws GiluException {
        if (description.isEmpty()) {
            throw new GiluException("Oops! I need some details for your ToDo. What should I remind you about?");
        }
        tasks[taskCount++] = new Todo(description);
        printAddedTask();
    }

    // Adds a Deadline task
    private static void addDeadline(String input) throws GiluException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new GiluException("Whoops! Your deadline is missing something. Try: deadline <task> /by <date>.");
        }
        tasks[taskCount++] = new Deadline(parts[0], parts[1]);
        printAddedTask();
    }

    // Adds an Event task
    private static void addEvent(String input) throws GiluException {
        String[] parts = input.split(" /from ", 2);
        if (parts.length < 2) {
            throw new GiluException("Your event needs details! Use: event <task> /from <start> /to <end>");
        }
        String[] timeParts = parts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            throw new GiluException("Oops! Your event needs both a start and end time. Try: event <task> /from <start> /to <end>");
        }
        tasks[taskCount++] = new Event(parts[0], timeParts[0], timeParts[1]);
        printAddedTask();
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
