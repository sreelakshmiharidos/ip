package gilu;

import gilu.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the Storage class, particularly the loadTasks method.
 */
class StorageTest {
    private static final String TEST_FILE_PATH = "./data/gilu_test.txt";
    private Storage storage;

    @BeforeEach
    void setUp() throws IOException {
        // Ensure the test file is clean before each test
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        Files.createFile(Paths.get(TEST_FILE_PATH));
        storage = new Storage(TEST_FILE_PATH);
    }

    /**
     * Test: loadTasks should correctly load tasks from a file with valid data.
     */
    @Test
    void testLoadTasksFromFile() throws IOException {
        // Prepare test file with valid data
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("T | 0 | Read book\n");
            writer.write("D | 1 | Submit assignment | 2023-12-15 1800\n");
            writer.write("E | 0 | Team meeting | 2023-12-10 1400 | 2023-12-12 1600\n");
        }

        // Load tasks from file
        List<Task> tasks = storage.loadTasks();

        // Assertions
        assertEquals(3, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
        assertTrue(tasks.get(1) instanceof Deadline);
        assertTrue(tasks.get(2) instanceof Event);

        assertEquals("Read book", tasks.get(0).getDescription());
        assertFalse(tasks.get(0).isDone());

        assertEquals("Submit assignment", tasks.get(1).getDescription());
        assertTrue(tasks.get(1).isDone());
        assertEquals(LocalDateTime.of(2023, 12, 15, 18, 0), ((Deadline) tasks.get(1)).getBy());

        assertEquals("Team meeting", tasks.get(2).getDescription());
        assertFalse(tasks.get(2).isDone());
        assertEquals(LocalDateTime.of(2023, 12, 10, 14, 0), ((Event) tasks.get(2)).getFrom());
        assertEquals(LocalDateTime.of(2023, 12, 12, 16, 0), ((Event) tasks.get(2)).getTo());
    }

    /**
     * Test: loadTasks should return an empty list if the file is empty.
     */
    @Test
    void testLoadTasksFromEmptyFile() throws IOException {
        // Ensure the test file is empty
        Files.writeString(Paths.get(TEST_FILE_PATH), "");

        // Load tasks from file
        List<Task> tasks = storage.loadTasks();

        // Assertions
        assertTrue(tasks.isEmpty());
    }

    /**
     * Test: loadTasks should throw an IOException for corrupted file data.
     */
    @Test
    void testLoadTasksFromCorruptedFile() throws IOException {
        // Prepare test file with corrupted data
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("Invalid data format\n");
        }

        // Assertions
        IOException exception = assertThrows(IOException.class, () -> storage.loadTasks());
        assertEquals("The file format is corrupted. Please fix or delete the file.", exception.getMessage());
    }

    /**
     * Test: loadTasks should handle missing fields gracefully.
     */
    @Test
    void testLoadTasksFromFileWithMissingFields() throws IOException {
        // Prepare test file with missing fields
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("D | 1 | Submit assignment\n"); // Missing deadline
        }

        // Assertions
        IOException exception = assertThrows(IOException.class, () -> storage.loadTasks());
        assertTrue(exception.getMessage().contains("The file format is corrupted"));
    }
}
