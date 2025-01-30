import java.util.Scanner;

public class Gilu {
    public static void main(String[] args) {
        // welcome message
        printLine();
        System.out.println(" Heyoo! I'm Gilu, your trusted friend!");
        System.out.println(" How can I make your day better?");
        printLine();

        Scanner scanner = new Scanner(System.in);

        // loop to process input
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                // exit message
                printLine();
                System.out.println(" Bye for now! But I hope to see you again soon!");
                printLine();
                break;
            }

            // echo input
            printLine();
            System.out.println(" " + input);
            printLine();
        }

        scanner.close();
    }

    // helper method that prints horizontal squiggly lines
    private static void printLine() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
