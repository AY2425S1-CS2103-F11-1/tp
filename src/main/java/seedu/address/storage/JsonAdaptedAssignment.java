package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.AssignmentName;
import seedu.address.model.student.Student;

/**
 * Jackson-friendly version of {@link Assignment}.
 */
class JsonAdaptedAssignment {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Assignment's %s field is missing!";

    private final String assignmentName;
    private final int maxScore;
    private final int score;
    private final boolean hasSubmitted;

    /**
     * Constructs a {@code JsonAdaptedAssignment} with the given assignment details.
     */
    @JsonCreator
    public JsonAdaptedAssignment(@JsonProperty("assignmentName") String assignmentName,
                                 @JsonProperty("maxScore") int maxScore,
                                 @JsonProperty("score") int score,
                                 @JsonProperty("hasSubmitted") boolean hasSubmitted) {
        this.assignmentName = assignmentName;
        this.maxScore = maxScore;
        this.score = score;
        this.hasSubmitted = hasSubmitted;
    }

    /**
     * Converts a given {@code Assignment} into this class for Jackson use.
     */
    public JsonAdaptedAssignment(Assignment source) {
        this.assignmentName = source.getName();
        this.maxScore = source.getMaxScore();
        this.score = source.getScore();
        this.hasSubmitted = source.getHasSubmitted();
    }

    /**
     * Converts this Jackson-friendly adapted assignment object into the model's {@code Assignment} object.
     *
     * @param student The {@code Student} to which this assignment belongs.
     * @throws IllegalValueException if there were any data constraints violated in the adapted assignment.
     */
    public Assignment toModelType(Student student) throws IllegalValueException {
        if (assignmentName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Assignment Name"));
        }
        if (!AssignmentName.isValidName(assignmentName)) { // Assuming you have this validation
            throw new IllegalValueException("Invalid Assignment Name");
        }
        if (!Assignment.isValidMaxScore(maxScore)) {
            throw new IllegalValueException(Assignment.MAX_SCORE_MESSAGE_CONSTRAINTS);
        }

        final AssignmentName modelAssignmentName = new AssignmentName(assignmentName);
        Assignment assignment = new Assignment(student, modelAssignmentName, maxScore);
        assignment.setScore(score);
        assignment.setHasSubmitted(hasSubmitted);
        return assignment;
    }
}
