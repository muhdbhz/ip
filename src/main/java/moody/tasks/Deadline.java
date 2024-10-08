package moody.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Represents a deadline task with a specific due date and time.
 * A Deadline is a type of Task that includes a date and time by which the task is due.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
    protected LocalDateTime by;

    /**
     * Creates a Deadline with the specified description and due date.
     *
     * @param description The description of the Deadline.
     * @param by The due date and time for the Deadline.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Converts the Deadline to a format suitable for saving to a file.
     * The format includes the task type, description, and due date in a specific format.
     *
     * @return A string representation of the Deadline in file format.
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + this.by.format(INPUT_FORMATTER)
                + " | " + this.tags;
    }

    /**
     * Returns a string representation of the Deadline for display purposes.
     * The format includes the task type, description, due date in a user-friendly format
     * and tags (if any).
     *
     * @return A string representation of the Deadline.
     */
    @Override
    public String toString() {
        String modifiedTags = this.tags.isEmpty()
                ? ""
                : this.tags.stream()
                .map(tag -> "#" + tag)
                .collect(Collectors.joining(" "));

        String timeframeWithTags = String.format("(by: %s) %s",
                this.by.format(OUTPUT_FORMATTER), modifiedTags);
        return String.format("[D]%s %s", super.toString(), timeframeWithTags);
    }
}
