package gilu;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading of tasks to/from the disk.
 */
public class Storage {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private final String filePath;

    /**
     * Constructs a Storage object with the given file path.
     *
     * @param filePath The file path where tasks are stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    /**
     * Ensures that the storage file and its directories exist.
     */
    private void ensureFileExists() {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Create directories if needed
                file.createNewFile(); // Create file
            }
        } catch (IOException e) {
            System.out.println("Error creating storage file: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the file. If the file does not exist, an empty list is returned.
     *
     * @return A list of tasks loaded from the file.
     * @throws IOException If an I/O error occurs.
     */
    public List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks; // Return empty list if file does not exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IOException("The file format is corrupted. Please fix or delete the file.");
        }

        return tasks;
    }

    /**
     * Saves the given list of tasks to the file.
     *
     * @param tasks The list of tasks to save.
     * @throws IOException If an I/O error occurs.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(formatTask(task));
                writer.newLine();
            }
        }
    }

    /**
     * Parses a line from the file into a Task object.
     *
     * @param line A line from the file.
     * @return A Task object or null if the line is invalid.
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid task format: " + line);
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        try {
            switch (type) {
                case "T":
                    return new Todo(description, isDone);
                case "D":
                    LocalDateTime by = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                    return new Deadline(description, by, isDone);
                case "E":
                    LocalDateTime from = LocalDateTime.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                    LocalDateTime to = LocalDateTime.parse(parts[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                    return new Event(description, from, to, isDone);
                default:
                    throw new IllegalArgumentException("Unknown task type: " + type);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing task date: " + line);
        }
    }

    /**
     * Formats a Task object into a file-friendly string.
     *
     * @param task The Task object.
     * @return A string representing the task.
     */
    private String formatTask(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        if (task instanceof Todo) {
            return "T | " + (task.isDone() ? "1" : "0") + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + (task.isDone() ? "1" : "0") + " | " + task.getDescription() + " | " + deadline.getBy().format(formatter);
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + (task.isDone() ? "1" : "0") + " | " + task.getDescription() + " | " + event.getFrom().format(formatter) + " | " + event.getTo().format(formatter);
        }
        throw new IllegalArgumentException("Unknown task type: " + task);
    }

    /**
     * Parses a date string into a LocalDateTime object.
     *
     * @param dateString The date string to parse.
     * @return The parsed LocalDateTime object.
     */
    private LocalDateTime parseDate(String dateString) {
        return LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
    }
}
