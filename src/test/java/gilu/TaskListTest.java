package gilu;

import gilu.exception.GiluException;
import gilu.storage.Storage;
import gilu.task.Deadline;
import gilu.task.Task;
import gilu.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the TaskList class, particularly the addDeadline method.
 */
class TaskListTest {
    private TaskList taskList;
    private Ui ui;
    private Storage storage;

    /**
     * Sets up a fresh TaskList and Storage instance before each test.
     * Ensures the test storage file starts clean.
     *
     * @throws IOException If an error occurs during setup.
     */
    @BeforeEach
    void setUp() throws IOException {
        ui = new Ui();
        storage = new Storage("./data/test_tasks.txt"); // Use a separate file for tests
        taskList = new TaskList();
        storage.saveTasks(taskList.getTasks());
    }

    /**
     * Tests that a valid deadline task is added to the TaskList correctly.
     *
     * @throws GiluException If the task format is invalid.
     * @throws IOException    If an error occurs while saving tasks.
     */
    @Test
    void testAddDeadlineValidInput() throws GiluException, IOException {
        String input = "return book /by 2023-12-15 1800";
        taskList.addDeadline(input, ui, storage);

        // Verify the task list has one Deadline task
        assertEquals(1, taskList.getTasks().size(), "Task list size should be 1");
        Task task = taskList.getTasks().get(0);
        assertTrue(task instanceof Deadline, "Task should be a Deadline instance");

        // Verify the Deadline details
        Deadline deadline = (Deadline) task;
        assertEquals("return book", deadline.getDescription(), "Description should match");
        assertEquals(LocalDateTime.parse("2023-12-15 1800", DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")), deadline.getBy(), "Deadline date should match");
    }

    /**
     * Tests that a GiluException is thrown when the /by part or deadline details are missing.
     */
    @Test
    void testAddDeadlineMissingDetails() {
        String input = "return book"; // Missing /by part
        GiluException exception = assertThrows(GiluException.class, () -> taskList.addDeadline(input, ui, storage));
        assertEquals("Whoops! Your deadline is missing something. Try: deadline <task> /by <yyyy-MM-dd HHmm>.", exception.getMessage());
    }

    /**
     * Tests that a GiluException is thrown for an invalid date format.
     */
    @Test
    void testAddDeadlineInvalidDateFormat() {
        String input = "return book /by 15-12-2023 1800"; // Invalid date format
        GiluException exception = assertThrows(GiluException.class, () -> taskList.addDeadline(input, ui, storage));
        assertEquals("Invalid date format! Use: yyyy-MM-dd HHmm (e.g., 2023-12-15 1800)", exception.getMessage());
    }
}
