package gilu;

import gilu.exception.GiluException;
import java.io.IOException;

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
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            return parser.executeCommand(input, tasks, ui, storage);  // Now returns a String
        } catch (GiluException e) {
            return e.getMessage();
        }
    }
}
