package gilu;

import gilu.exception.GiluException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Gilu is a chatbot that helps manage tasks.
 * Tasks are stored persistently using file-based storage.
 */
public class Gilu {
    private static final String DEFAULT_STORAGE_PATH = "./data/gilu.txt";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    /**
     * Constructs a Gilu chatbot instance with the given storage path.
     *
     * @param filePath The file path to store tasks.
     */
    public Gilu(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showMessage("Error loading tasks: " + e.getMessage());
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Runs the chatbot, handling user input and executing commands.
     */
    public void run() {
        ui.showWelcomeMessage();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            String input = scanner.nextLine().trim();
            try {
                isRunning = parser.executeCommand(input, tasks, ui, storage);
            } catch (GiluException e) {
                ui.showMessage(e.getMessage());
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        String storagePath = (args.length > 0) ? args[0] : DEFAULT_STORAGE_PATH;

        // If a test file is used, clear it before loading tasks
        if (args.length > 0) {
            try {
                new FileWriter(storagePath, false).close(); // Truncate test file
            } catch (IOException e) {
                System.out.println("Error clearing test file: " + e.getMessage());
            }
        }

        new Gilu(storagePath).run();
    }
}
