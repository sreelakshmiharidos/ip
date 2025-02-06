package gilu;

/**
 * Represents a Deadline task.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Constructs a Deadline with the given description and deadline date.
     *
     * @param description The description of the Deadline.
     * @param by The deadline date/time.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Constructs a Deadline with the given description, deadline date, and completion status.
     *
     * @param description The description of the Deadline.
     * @param by The deadline date/time.
     * @param isDone Whether the Deadline is marked as done.
     */
    public Deadline(String description, String by, boolean isDone) {
        super(description);
        this.by = by;
        this.isDone = isDone;
    }

    /**
     * Gets the deadline date/time.
     *
     * @return The deadline date/time.
     */
    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
