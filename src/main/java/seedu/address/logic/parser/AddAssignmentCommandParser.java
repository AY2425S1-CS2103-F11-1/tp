package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT_MAX_SCORE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAssignmentCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.AssignmentName;

/**
 * Parses input arguments and creates a new AddAssignmentCommand object
 */
public class AddAssignmentCommandParser implements Parser<AddAssignmentCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddAssignmentCommand
     * and returns an AddAssignmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAssignmentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STUDENT_INDEX, PREFIX_ASSIGNMENT_NAME,
                        PREFIX_ASSIGNMENT_MAX_SCORE);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENT_INDEX, PREFIX_ASSIGNMENT_NAME, PREFIX_ASSIGNMENT_MAX_SCORE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAssignmentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENT_INDEX, PREFIX_ASSIGNMENT_NAME,
                PREFIX_ASSIGNMENT_MAX_SCORE);

        Index studentIndex;
        try {
            studentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STUDENT_INDEX).get());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }
        AssignmentName assignmentName = ParserUtil.parseAssignmentName(argMultimap.getValue(PREFIX_ASSIGNMENT_NAME)
                .get());
        int maxScore = ParserUtil.parseMaxScore(argMultimap.getValue(PREFIX_ASSIGNMENT_MAX_SCORE).get());
        AddAssignmentCommand.AssignmentDescriptor assignmentDescriptor =
                new AddAssignmentCommand.AssignmentDescriptor(maxScore, assignmentName);
        return new AddAssignmentCommand(studentIndex, assignmentDescriptor);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
