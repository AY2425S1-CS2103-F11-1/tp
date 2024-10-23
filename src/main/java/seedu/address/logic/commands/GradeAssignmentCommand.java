package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT_SCORE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.student.Student;

/**
 * Edits the details of score of an existing assignment belonging to an existing student in the app.
 */
public class GradeAssignmentCommand extends Command {
    public static final String COMMAND_WORD = "grade";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the score of the assignment belonging to a "
            + "student by the student's index number used in the displayed student list, and the assignment's index"
            + " belonging to the student. "
            + "The assignment will also be marked as Submitted.\n"
            + "Parameters: "
            + "[" + PREFIX_STUDENT_INDEX + "INDEX] "
            + "[" + PREFIX_ASSIGNMENT_INDEX + "INDEX] "
            + "[" + PREFIX_ASSIGNMENT_SCORE + "ASSIGNMENT SCORE]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_STUDENT_INDEX + "1 "
            + PREFIX_ASSIGNMENT_INDEX + "1 "
            + PREFIX_ASSIGNMENT_SCORE + "100";

    public static final String MESSAGE_GRADE_SUCCESS = "Graded Assignment: %1$s belonging "
            + "to Student: %2$s, with score: %3$s";
    private final Index studentIndex;
    private final Index assignmentIndex;
    private final int score;

    /**
     * @param studentIndex of the student in the filtered student list
     * @param assignmentIndex of the assignment belonging to the assignmentList of the student
     * @param score of the assignment
     */
    public GradeAssignmentCommand(Index studentIndex, Index assignmentIndex, int score) {
        assert studentIndex != null;
        assert assignmentIndex != null;
        requireNonNull(studentIndex);
        requireNonNull(assignmentIndex);
        this.studentIndex = studentIndex;
        this.assignmentIndex = assignmentIndex;
        this.score = score;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (studentIndex.getZeroBased() >= lastShownList.size() || studentIndex.getZeroBased() < 0) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }
        Student studentToGrade = lastShownList.get(studentIndex.getZeroBased());
        List<Assignment> assignmentList = studentToGrade.getAssignmentList();

        if (assignmentIndex.getZeroBased() >= assignmentList.size() || assignmentIndex.getZeroBased() < 0) {
            throw new CommandException(Messages.MESSAGE_INVALID_ASSIGNMENT_INDEX);
        }
        Assignment assignmentToGrade = assignmentList.get(assignmentIndex.getZeroBased());

        if (assignmentToGrade.getMaxScore() < score) {
            throw new CommandException(Messages.MESSAGE_SCORE_EXCEEDS_MAX_SCORE);
        }
        if (score < 0) {
            throw new CommandException(Messages.MESSAGE_NEGATIVE_SCORE);
        }
        assignmentToGrade.setScore(score);
        assignmentToGrade.setHasSubmitted(true);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(String.format(MESSAGE_GRADE_SUCCESS, assignmentToGrade.getName(),
                studentToGrade.getName().fullName, assignmentToGrade.getScore()),
                studentToGrade, studentIndex.getZeroBased());
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        //instanceof handles null
        if (!(other instanceof GradeAssignmentCommand)) {
            return false;
        }
        GradeAssignmentCommand otherGradeAssignmentCommand = (GradeAssignmentCommand) other;
        return studentIndex.equals(otherGradeAssignmentCommand.studentIndex)
                && assignmentIndex.equals(otherGradeAssignmentCommand.assignmentIndex)
                && score == ((GradeAssignmentCommand) other).score;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("studentIndex", studentIndex)
                .add("assignmentIndex", assignmentIndex)
                .add("score", score)
                .toString();
    }
}
