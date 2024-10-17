package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.AssignmentName;
import seedu.address.model.student.Student;
import seedu.address.testutil.AssignmentBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddAssignmentCommand}.
 */
public class AddAssignmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullStudentAndAssignment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddAssignmentCommand(null, null));
    }

    @Test
    public void execute_assignmentAcceptedByModel_addSuccessful() throws Exception {
        AddAssignmentCommand.AssignmentDescriptor toAddDescriptor =
                new AddAssignmentCommand.AssignmentDescriptor(AssignmentBuilder.DEFAULT_MAX_SCORE,
                        new AssignmentName(AssignmentBuilder.DEFAULT_ASSIGNMENT_NAME));
        AddAssignmentCommand addAssignmentCommand = new AddAssignmentCommand(INDEX_FIRST_STUDENT, toAddDescriptor);
        CommandResult commandResult = addAssignmentCommand.execute(model);

        Student studentToAssignAssignmentTo = model.getFilteredStudentList().get(0);
        Assignment expectedAssignment = new AssignmentBuilder().withStudent(studentToAssignAssignmentTo).build();
        studentToAssignAssignmentTo = studentToAssignAssignmentTo.addAssignment(expectedAssignment);
        expectedAssignment = studentToAssignAssignmentTo.getAssignmentList().get(0);
        String expectedMessage = String.format(AddAssignmentCommand.MESSAGE_SUCCESS,
                Messages.format(expectedAssignment));

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        // Fails because of bug
        assertEquals(expectedAssignment, commandResult.getStudent().getAssignmentList()
                .get(0));
    }
}
