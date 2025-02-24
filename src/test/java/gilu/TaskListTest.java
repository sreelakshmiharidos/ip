package gilu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gilu.exception.GiluException;
import gilu.storage.Storage;
import gilu.task.Deadline;
import gilu.task.Task;
import gilu.ui.Ui;


/**
 * Tests the functionality of the TaskList class, particularly the addDeadline method.
 */
class TaskListTest {
    private static final String TEST_STORAGE_PATH = "./data/test_tasks.txt";
    private static final String VALID_INPUT = "deadline return book /by 2023-12-15 1800";
    private static final String INVALID_DATE_INPUT = "deadline return book /by 15-12-2023 1800";
    private static final String MISSING_DETAILS_INPUT = "deadline return book";

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
        storage = new Storage(TEST_STORAGE_PATH);
        taskList = new TaskList();
        storage.saveTasks(new ArrayList<>()); // Ensures clean test file
    }

    /**
     * Tests that a valid deadline task is successfully added to the {@link TaskList}.
     *
     * <p>This test ensures that:</p>
     * <ul>
     *   <li>The task list contains exactly one task after addition.</li>
     *   <li>The task is an instance of {@link Deadline}.</li>
     *   <li>The task has the correct description.</li>
     * </ul>
     *
     * @throws GiluException If the task format is invalid.
     * @throws IOException If an error occurs while saving tasks.
     */
    @Test
    void testAddDeadlineValidInput() throws GiluException, IOException {
        String input = "return book /by 2023-12-15 1800";
        taskList.addDeadline(input, ui, storage);

        // Check if task list contains 1 task
        assertEquals(1, taskList.getTasks().size(), "Task list size should be 1");

        // Verify the added task is a Deadline with the correct description
        Task task = taskList.getTasks().get(0);
        assertTrue(task instanceof Deadline, "Task should be a Deadline instance");
        assertEquals("return book", task.getDescription(), "Description should match");
    }

    /**
     * Tests that a GiluException is thrown when the /by part or deadline details are missing.
     */
    @Test
    void testAddDeadlineMissingDetails() {
        GiluException exception = assertThrows(GiluException.class, () ->
                taskList.addDeadline(MISSING_DETAILS_INPUT, ui, storage));
        assertEquals("Your deadline is missing something. Try: deadline <task> /by <yyyy-MM-dd HHmm>.",
                exception.getMessage());
    }

    /**
     * Tests that a GiluException is thrown for an invalid date format.
     */
    @Test
    void testAddDeadlineInvalidDateFormat() {
        GiluException exception = assertThrows(GiluException.class, () ->
                taskList.addDeadline(INVALID_DATE_INPUT, ui, storage));
        assertEquals("Invalid date format! Use: yyyy-MM-dd HHmm.", exception.getMessage());
    }
}
