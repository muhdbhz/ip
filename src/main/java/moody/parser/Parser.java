package moody.parser;

import moody.exceptions.InvalidCommandException;
import moody.exceptions.TaskInputException;
import moody.commands.*;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into Command objects.
 * The Parser class is responsible for interpreting the commands provided by the user
 * and returning the corresponding Command objects.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses the given command string into a Command object.
     * The method splits the command string into the command keyword and arguments,
     * then creates and returns the appropriate Command based on the keyword.
     *
     * @param input The command string to parse.
     * @return The Command object corresponding to the parsed command.
     * @throws InvalidCommandException If the command is not recognized.
     * @throws TaskInputException If there is an issue with the task input.
     */
    public static Command parse(String input) throws InvalidCommandException, TaskInputException {
        String[] parts = input.split(" ", 2); // Split into command and arguments

        String commandWord = parts[0].toLowerCase(); // Get the command keyword
        String arguments = parts.length > 1 ? parts[1] : ""; // Get the arguments

        switch (commandWord) {
        case "bye":
            return new ExitCommand();

        case "list":
            return new ListCommand();

        case "mark":
            return parseMarkCommand(arguments);

        case "unmark":
            return parseUnmarkCommand(arguments);

        case "todo":
            return parseTodoCommand(arguments);

        case "deadline":
            return parseDeadlineCommand(arguments);

        case "event":
            return parseEventCommand(arguments);

        case "delete":
            return parseDeleteCommand(arguments);

        case "find":
            return parseFindCommand(arguments);

        default:
            throw new InvalidCommandException("""
            Error: Command not found
               
            Please input a valid command
            """);
        }
    }

    /**
     * Parses the arguments for a "mark" command into a MarkCommand object.
     * The method extracts the task index from the arguments and creates a MarkCommand.
     *
     * @param arguments The arguments of the "mark" command, expected to be the task index.
     * @return A MarkCommand object with the specified task index.
     * @throws TaskInputException If the task index is not a valid number.
     */
    private static Command parseMarkCommand(String arguments) throws TaskInputException {
        int taskIndex = parseTaskIndex(arguments);

        return new MarkCommand(taskIndex);
    }

    /**
     * Parses the arguments for an "unmark" command into an UnmarkCommand object.
     * The method extracts the task index from the arguments and creates an UnmarkCommand.
     *
     * @param arguments The arguments of the "unmark" command, expected to be the task index.
     * @return An UnmarkCommand object with the specified task index.
     * @throws TaskInputException If the task index is not a valid number.
     */
    private static Command parseUnmarkCommand(String arguments) throws TaskInputException {
        int taskIndex = parseTaskIndex(arguments);

        return new UnmarkCommand(taskIndex);
    }

    /**
     * Parses the arguments for a "todo" command into an AddTodoCommand object.
     * The method extracts the description from the arguments and creates an AddTodoCommand.
     *
     * @param arguments The arguments of the "todo" command, expected to be the task description.
     * @return An AddTodoCommand object with the specified description.
     * @throws TaskInputException If the description is empty.
     */
    private static Command parseTodoCommand(String arguments) throws TaskInputException {
        if (arguments.isEmpty()) {
            throw new TaskInputException("Error: The description of a todo cannot be empty.");
        }
        return new AddTodoCommand(arguments);
    }

    /**
     * Parses the arguments for a "deadline" command into an AddDeadlineCommand object.
     * The method extracts the description and due date from the arguments and creates an AddDeadlineCommand.
     *
     * @param arguments The arguments of the "deadline" command, expected to include the description and due date.
     * @return An AddDeadlineCommand object with the specified description and due date.
     * @throws TaskInputException If the description or date is missing or the date format is invalid.
     */
    private static Command parseDeadlineCommand(String arguments) throws TaskInputException {
        String[] parts = arguments.split(" /by ", 2);
        if (parts.length != 2) {
            throw new TaskInputException("""
                    Error: The description and date of a deadline cannot be empty.

                    Please use the following format: deadline <description> /by <date>
                    """);
        }

        String description = parts[0].trim();
        String dateTimeString = parts[1].trim();

        try {
            return new AddDeadlineCommand(description, dateTimeString);
        } catch (DateTimeParseException e) {
            throw new TaskInputException("Error: Invalid date format. Please use yyyy-MM-dd HHmm.");
        }
    }

    /**
     * Parses the arguments for an "event" command into an AddEventCommand object.
     * The method extracts the description, start date, and end date from the arguments and creates an AddEventCommand.
     *
     * @param arguments The arguments of the "event" command, expected to include the description, start date, and end date.
     * @return An AddEventCommand object with the specified description, start date, and end date.
     * @throws TaskInputException If the description, start date, or end date is missing.
     */
    private static Command parseEventCommand(String arguments) throws TaskInputException {
        String[] parts = arguments.split(" /from | /to ", 3);
        if (parts.length != 3) {
            throw new TaskInputException("""
                    Error: The description and dates of an event cannot be empty.
                    
                    Please use the following format: event <name> /from <date> <time> /to <date> <time>
                    """);
        }

        String description = parts[0].trim();
        String fromString = parts[1].trim();
        String toString = parts[2].trim();

        return new AddEventCommand(description, fromString, toString);
    }

    /**
     * Parses the arguments for a "delete" command into a DeleteCommand object.
     * The method extracts the task index from the arguments and creates a DeleteCommand.
     *
     * @param arguments The arguments of the "delete" command, expected to be the task index.
     * @return A DeleteCommand object with the specified task index.
     * @throws TaskInputException If the task index is not a valid number.
     */
    private static Command parseDeleteCommand(String arguments) throws TaskInputException {
        int taskIndex = parseTaskIndex(arguments);

        return new DeleteCommand(taskIndex);
    }

    private static Command parseFindCommand(String arguments) throws TaskInputException {
        if (arguments.trim().isEmpty()) {
            throw new TaskInputException("Error: The search keyword cannot be empty.");
        }
        return new FindCommand(arguments.trim());
    }

    /**
     * Parses the task index from the provided string argument.
     *
     * @param arguments The string containing the task number provided by the user.
     * @return The zero-based index of the task.
     * @throws TaskInputException If the provided argument is not a valid number.
     */
    private static int parseTaskIndex(String arguments) throws TaskInputException {
        try {
            return Integer.parseInt(arguments.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new TaskInputException("Error: Invalid task number.");
        }
    }
}
