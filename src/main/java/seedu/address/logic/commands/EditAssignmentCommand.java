package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;

public class EditAssignmentCommand extends Command {
    public static final String COMMAND_WORD = "edit_assignment";
    public static final String MESSAGE_SUCCESS = "Successfully updated the submission status of assignment";
    public static final String MESSAGE_ASSIGNMENT_NOT_FOUND = "There is no such assignment in the list!";
    private final Assignment toEdit;

    public EditAssignmentCommmand(Assignment assignment) {
        requireNonNull(assignment);
        toEdit = assignment;
        // maybe can change this to index of assignment in the list instead
    }

    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasAssignment(toEdit)) {
            throw new CommandException(MESSAGE_ASSIGNMENT_NOT_FOUND);
        }
        model.editAssignmentStatus(toEdit);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
