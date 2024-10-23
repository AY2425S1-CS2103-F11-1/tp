package seedu.address.model.assignment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.assignment.exceptions.ScoreExceedsMaxScoreException;
import seedu.address.model.student.Student;

/**
 * Represents an Assignment in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Assignment {

    public static final String MAX_SCORE_MESSAGE_CONSTRAINTS =
            "The maximum score should be greater than or equal to the minimum score of 0";
    public static final String SCORE_MESSAGE_CONSTRAINTS =
            "The score should be between (inclusive) of the minimum and maximum score";

    private static final int MIN_SCORE = 0;

    // Identity fields
    private final AssignmentName assignmentName;

    // Data fields
    private final int maxScore;
    private int score = 0;
    private boolean hasSubmitted = false;
    private Student student;

    /**
     * Every field must be present and not null.
     */
    public Assignment(Student student, AssignmentName name, int maxScore) {
        requireNonNull(student);
        requireNonNull(name);
        this.student = student;
        this.assignmentName = name;
        checkArgument(isValidMaxScore(maxScore), MAX_SCORE_MESSAGE_CONSTRAINTS);
        this.maxScore = maxScore;
    }

    /**
     * Returns true if a given maxScore is a valid score.
     */
    public static boolean isValidMaxScore(int test) {
        return test >= MIN_SCORE;
    }

    /**
     * Returns true if both assignments have the same assignmentName.
     * This defines a weaker notion of equality between two assignments.
     */
    public boolean isSameAssignment(Assignment otherAssignment) {
        if (otherAssignment == this) {
            return true;
        }

        return otherAssignment != null
                && otherAssignment.assignmentName.equals(assignmentName);
    }

    /**
     * Returns true if both assignments have the same identity and data fields.
     * This defines a stronger notion of equality between two assignments.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Assignment)) {
            return false;
        }

        Assignment otherAssignment = (Assignment) other;
        return student.equals(otherAssignment.student)
                && assignmentName.equals(otherAssignment.assignmentName)
               && MIN_SCORE == otherAssignment.MIN_SCORE
               && maxScore == otherAssignment.maxScore
               && score == otherAssignment.score
               && hasSubmitted == otherAssignment.hasSubmitted
               && student.equals(otherAssignment.student);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(assignmentName, MIN_SCORE, maxScore, score, hasSubmitted);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("student", student)
                .add("assignmentName", assignmentName)
                .add("studentName", this.student == null ? "Not Assigned" : student.getName())
                .add("MIN_SCORE", MIN_SCORE)
                .add("maxScore", maxScore)
                .add("score", score)
                .add("hasSubmitted", hasSubmitted)
                .toString();
    }
    public String getName() {
        return this.assignmentName.assignmentName;
    }
    public int getScore() {
        return this.score;
    }
    public int getMaxScore() {
        return this.maxScore;
    }
    public boolean getHasSubmitted() {
        return this.hasSubmitted;
    }
    public Student getStudent() {
        return this.student;
    }

    public void setScore(int score) {
        if (score > this.getMaxScore()) {
            throw new ScoreExceedsMaxScoreException();
        }
        this.score = score;
    }
    public void setHasSubmitted(boolean hasSubmitted) {
        this.hasSubmitted = hasSubmitted;
    }
}
