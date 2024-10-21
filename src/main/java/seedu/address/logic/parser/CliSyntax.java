package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_ASSIGNMENT_NAME = new Prefix("assignmentName/");
    public static final Prefix PREFIX_ASSIGNMENT_SCORE = new Prefix("score/");
    public static final Prefix PREFIX_STUDENT_INDEX = new Prefix("studentIndex/");
    public static final Prefix PREFIX_ASSIGNMENT_INDEX = new Prefix("assignmentIndex/");
    public static final Prefix PREFIX_ASSIGNMENT_MAX_SCORE = new Prefix("maxScore/");
    public static final Prefix PREFIX_REMARK = new Prefix("r/");
}
