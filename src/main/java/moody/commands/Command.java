package moody.commands;

import moody.exceptions.InvalidCommandException;
import moody.exceptions.TaskInputException;
import moody.storage.Storage;
import moody.tasks.TaskList;
import moody.ui.Ui;

import java.io.IOException;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws InvalidCommandException,
            IOException, TaskInputException;
    public boolean isExit() {
        return false;
    }
}
