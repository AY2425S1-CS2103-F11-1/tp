package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.student.Student;

/**
 * A UI component that expands the information of a selected {@code Student}.
 */
public class StudentDetailsPanel extends UiPart<Region> {

    private static final String FXML = "StudentDetailsPanel.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Student student;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private FlowPane detailsTags;
    @FXML
    private FlowPane assignments;
    @FXML
    private Label remark;

    /**
     * Creates a {@code StudentCode} with the given {@code Student} and index to display.
     */
    public StudentDetailsPanel(Student student, int displayedIndex) {
        super(FXML);
        this.student = student;
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        phone.setText(student.getPhone().value);
        email.setText(student.getEmail().value);
        remark.setText(student.getRemark());
        student.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> detailsTags.getChildren().add(new Label(tag.tagName)));

        // Displaying assignments
        student.getAssignmentList().stream().forEach(assignment -> {
            VBox assignmentBox = new VBox();
            assignmentBox.setSpacing(5); // Optional: add spacing between elements

            Label titleLabel = new Label(assignment.getName());
            titleLabel.getStyleClass().add("assignment-name");
            Label scoreLabel = new Label("Score: " + assignment.getScore() + "/" + assignment.getMaxScore());
            Label statusLabel = new Label(assignment.getHasSubmitted() ? "Submitted" : "Not Submitted");
            if (assignment.getHasSubmitted()) {
                statusLabel.getStyleClass().add("assignment-status-submitted");
            } else {
                statusLabel.getStyleClass().add("assignment-status-unsubmitted");
            }

            // Optionally apply CSS or styling to differentiate the assignment boxes
            assignmentBox.getStyleClass().add("assignment-box");

            // Add all labels to the VBox
            assignmentBox.getChildren().addAll(titleLabel, scoreLabel, statusLabel);

            // Add the VBox to the assignments FlowPane
            assignments.getChildren().add(assignmentBox);
        });
    }
}
