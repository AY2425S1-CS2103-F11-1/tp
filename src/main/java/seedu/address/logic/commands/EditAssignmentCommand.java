package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;

/**
 * Edits the submission status of an existing assignment in the list.
 */
public class EditAssignmentCommand extends Command {

    public static final String COMMAND_WORD = "edit_assignment";

    public static final String MESSAGE_SUCCESS = "Successfully updated the submission status of assignment";
    public static final String MESSAGE_ASSIGNMENT_NOT_FOUND = "There is no such assignment in the list!";

    private final Assignment toEdit;

    /**
     * Constructs an EditAssignmentCommand to edit the submission status of the specified {@code Assignment}.
     *
     * @param assignment The assignment whose submission status is to be edited.
     */
    public EditAssignmentCommand(Assignment assignment) {
        requireNonNull(assignment);
        toEdit = assignment;
    }

    /**
     * Executes the command and updates the submission status of the assignment in the model.
     *
     * @param model The model which contains the list of assignments.
     * @return A CommandResult indicating the success of the operation.
     * @throws CommandException if the assignment is not found in the model.
     */
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasAssignment(toEdit)) {
            throw new CommandException(MESSAGE_ASSIGNMENT_NOT_FOUND);
        }
        model.editAssignmentStatus(toEdit);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
