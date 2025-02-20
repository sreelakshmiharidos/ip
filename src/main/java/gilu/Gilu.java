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
     * Constructs a Gilu chatbot instance with the default storage path.
     */
    public Gilu() {
        this(DEFAULT_STORAGE_PATH);
    }

    /**
     * Constructs a Gilu chatbot instance with the given storage path.
     *
     * @param filePath The file path to store tasks.
     */
    public Gilu(final String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        this.tasks = loadTasks();
    }

    /**
     * Loads tasks from storage, handling potential I/O errors.
     *
     * @return The initialized TaskList.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showMessage("Error loading tasks: " + e.getMessage());
            return new TaskList(); // Return an empty list if loading fails
        }
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The user input message.
     * @return The chatbot's response.
     */
    public String getResponse(final String input) {
        try {
            return parser.executeCommand(input, tasks, ui, storage);
        } catch (GiluException e) {
            return e.getMessage();
        }
    }
}
