package gilu;

/**
 * Represents an Event task with a start and end time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Constructs an Event with the given description, start, and end times.
     *
     * @param description The description of the Event.
     * @param from The start time of the Event.
     * @param to The end time of the Event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Constructs an Event with the given description, start, end times, and completion status.
     *
     * @param description The description of the Event.
     * @param from The start time of the Event.
     * @param to The end time of the Event.
     * @param isDone Whether the Event is marked as done.
     */
    public Event(String description, String from, String to, boolean isDone) {
        super(description);
        this.from = from;
        this.to = to;
        this.isDone = isDone;
    }

    /**
     * Gets the start time of the Event.
     *
     * @return The start time of the Event.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Gets the end time of the Event.
     *
     * @return The end time of the Event.
     */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
