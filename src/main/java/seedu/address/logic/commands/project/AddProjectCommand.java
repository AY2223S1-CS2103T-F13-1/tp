package seedu.address.logic.commands.project;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.ui.Ui;

/**
 * Lists all persons in the address book to the user.
 */
public class AddProjectCommand extends ProjectCommand {

    public static final String COMMAND_FLAG = "-a";

    // TODO: Better message
    public static final String MESSAGE_SUCCESS = "Edited Project";

    // TODO: implement
    @Override
    public CommandResult execute(Model model, Ui ui) throws CommandException {
        return null;
    }
}