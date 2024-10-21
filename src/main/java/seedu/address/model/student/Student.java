package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.student.exceptions.AssignmentIndexOutOfRangeException;
import seedu.address.model.tag.Tag;

/**
 * Represents a Student in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student {
    private static final String EMPTY_REMARK = "";

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final String remark;
    //AssignmentList initially null
    private List<Assignment> assignmentList = new ArrayList<Assignment>();

    /**
     * Every field except assignmentList must be present and not null. Remark is empty
     */
    public Student(Name name, Phone phone, Email email, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.remark = EMPTY_REMARK;
    }

    /**
     * Every field except assignmentList must be present and not null.
     */
    public Student(Name name, Phone phone, Email email, Set<Tag> tags, String remark) {
        requireAllNonNull(name, phone, email, tags, remark);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.remark = remark;
    }

    /**
     * Creates a Student object with an AssignmentList
     */
    public Student(Name name, Phone phone, Email email, Set<Tag> tags, List<Assignment> assignmentList, String remark) {
        requireAllNonNull(name, phone, email, tags, remark);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.assignmentList = assignmentList;
        this.remark = remark;
    }
    /**
     * Copy an existing student object and changing the remark attribute
     */
    public Student(Student studentToCopy, String remark) {
        requireAllNonNull(studentToCopy.getName(), studentToCopy.getPhone(), studentToCopy.getEmail(),
                studentToCopy.getTags(), remark);
        this.name = studentToCopy.getName();
        this.phone = studentToCopy.getPhone();
        this.email = studentToCopy.getEmail();
        this.tags.addAll(studentToCopy.getTags());
        this.assignmentList = studentToCopy.getAssignmentList();
        this.remark = remark;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }
    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }
    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public String getRemark() {
        return remark;
    }
    /**
     * Returns true if both students have the same name.
     * This defines a weaker notion of equality between two students.
     */
    public boolean isSameStudent(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }

        return otherStudent != null
                && otherStudent.getName().equals(getName());
    }
    /**
     * Returns true if both students have the same identity and data fields.
     * This defines a stronger notion of equality between two students.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;
        return name.equals(otherStudent.name)
                && phone.equals(otherStudent.phone)
                && email.equals(otherStudent.email)
                && tags.equals(otherStudent.tags)
                && assignmentList.equals(otherStudent.assignmentList)
                && remark.equals(otherStudent.remark);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .add("assignments", assignmentList)
                .add("remark", remark)
                .toString();
    }

    /**
     * Verifies if the {@code student} has already been assigned an {@code assignment} with the same
     * {@code AssignmentName}
     */
    public boolean hasAssignment(Assignment otherAssignment) {
        Objects.requireNonNull(otherAssignment);
        for (Assignment assignment : assignmentList) {
            if (assignment.isSameAssignment(otherAssignment)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Creates a new Student that has the same existing data, but with an added {@code assignment}
     * @return a new {@code Student} object with the additional Assignment
     */
    public Student addAssignment(Assignment assignmentToAdd) {
        Objects.requireNonNull(assignmentToAdd);
        List<Assignment> newAssignmentlist = new ArrayList<Assignment>(assignmentList);
        newAssignmentlist.add(assignmentToAdd);
        return new Student(this.name, this.phone, this.email, this.tags, newAssignmentlist, this.remark);
    }
    /**
     * Deletes the Assignment at {@code index} in the student's assignmentList
     */
    public void deleteAssignment(int index) {
        if (assignmentList.size() < (index - 1) || index <= 0) {
            throw new AssignmentIndexOutOfRangeException();
        }
        assignmentList.remove(index - 1);
    }

}
