package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.FLAG_UNKNOWN_COMMAND;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_MISSING_ARGUMENTS;
import static seedu.address.logic.parser.ProjectCliSyntax.PREFIX_CLIENT_ID;
import static seedu.address.logic.parser.ProjectCliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.ProjectCliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.ProjectCliSyntax.PREFIX_PROJECT_ID;
import static seedu.address.logic.parser.ProjectCliSyntax.PREFIX_REPOSITORY;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.project.AddProjectCommand;
import seedu.address.logic.commands.project.DeleteProjectCommand;
import seedu.address.logic.commands.project.EditProjectCommand;
import seedu.address.logic.commands.project.ListProjectCommand;
import seedu.address.logic.commands.project.ProjectCommand;
import seedu.address.logic.commands.project.SortProjectCommand;
import seedu.address.logic.commands.project.SetProjectDefaultViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Deadline;
import seedu.address.model.Name;
import seedu.address.model.client.ClientId;
import seedu.address.model.issue.Issue;
import seedu.address.model.project.ProjectId;
import seedu.address.model.project.ProjectWithoutModel;
import seedu.address.model.project.Repository;


/**
 * A parser to parse any commands related to project
 */
public class ProjectCommandParser implements Parser<ProjectCommand> {
    /**
     * Parse any commands that have to do with Projects
     *
     * @param flag      flag used in command
     * @param arguments arguments used in command
     * @return a ProjectCommand
     * @throws ParseException
     */
    @Override
    public ProjectCommand parse(String flag, String arguments) throws ParseException {
        switch (flag.strip()) {
        case AddProjectCommand.COMMAND_FLAG:
            return parseAddProjectCommand(arguments);
        case EditProjectCommand.COMMAND_FLAG:
            return parseEditProjectCommand(arguments);
        case DeleteProjectCommand.COMMAND_FLAG:
            return parseDeleteProjectCommand(arguments);
        case SortProjectCommand.COMMAND_FLAG:
            return parseSortProjectCommand(arguments);
        case ListProjectCommand.COMMAND_FLAG:
            return parseListProjectCommand(arguments);
        case SetProjectDefaultViewCommand.COMMAND_FLAG:
            return parseSetProjectDefaultViewCommand(arguments);
        default:
            throw new ParseException(FLAG_UNKNOWN_COMMAND);
        }
    }



    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns true if any of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean anyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private static boolean onlyOnePrefixPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).filter(prefix -> argumentMultimap.getValue(prefix).isPresent()).count() == 1;
    }

    private AddProjectCommand parseAddProjectCommand(String arguments) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(arguments, PREFIX_NAME, PREFIX_CLIENT_ID,
                        PREFIX_REPOSITORY, PREFIX_DEADLINE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddProjectCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        ClientId clientId;
        Repository repository;
        Deadline deadline;

        if (!arePrefixesPresent(argMultimap, PREFIX_CLIENT_ID)) {
            clientId = new ClientId.EmptyClientId();
        } else {
            clientId = ParserUtil.parseClientId(argMultimap.getValue(PREFIX_CLIENT_ID).get());
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_REPOSITORY)) {
            repository = Repository.EmptyRepository.EMPTY_REPOSITORY;
        } else {
            repository = ParserUtil.parseRepository(argMultimap.getValue(PREFIX_REPOSITORY).get());
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_DEADLINE)) {
            deadline = Deadline.EmptyDeadline.EMPTY_DEADLINE;
        } else {
            deadline = ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DEADLINE).get());
        }

        List<Issue> issueList = new ArrayList<>();

        ProjectWithoutModel projectWithoutModel =
                new ProjectWithoutModel(name, repository, deadline, clientId, issueList);

        return new AddProjectCommand(projectWithoutModel);
    }

    private EditProjectCommand parseEditProjectCommand(String arguments) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(arguments, PREFIX_PROJECT_ID, PREFIX_NAME, PREFIX_CLIENT_ID,
                        PREFIX_REPOSITORY, PREFIX_DEADLINE);

        if (!arePrefixesPresent(argMultimap, PREFIX_PROJECT_ID) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditProjectCommand.MESSAGE_USAGE));
        }

        Name newName = null;
        ClientId newClientId = null;
        Repository newRepository = null;
        Deadline newDeadline = null;
        ProjectId newProjectId = ParserUtil.parseProjectId(argMultimap.getValue(PREFIX_PROJECT_ID).get());

        if (!anyPrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_CLIENT_ID, PREFIX_REPOSITORY, PREFIX_DEADLINE)) {
            throw new ParseException(String.format(MESSAGE_MISSING_ARGUMENTS,
                    EditProjectCommand.MESSAGE_USAGE));
        }

        if (arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            newName = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        }

        if (arePrefixesPresent(argMultimap, PREFIX_CLIENT_ID)) {
            newClientId = ParserUtil.parseClientId(argMultimap.getValue(PREFIX_CLIENT_ID).get());
        }

        if (arePrefixesPresent(argMultimap, PREFIX_REPOSITORY)) {
            newRepository = ParserUtil.parseRepository(argMultimap.getValue(PREFIX_REPOSITORY).get());
        }

        if (arePrefixesPresent(argMultimap, PREFIX_DEADLINE)) {
            newDeadline = ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DEADLINE).get());
        }

        return new EditProjectCommand(newProjectId, newName, newClientId, newRepository, newDeadline);
    }

    private DeleteProjectCommand parseDeleteProjectCommand(String arguments) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(arguments);
            return new DeleteProjectCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProjectCommand.MESSAGE_USAGE), pe);
        }
    }

    private SortProjectCommand parseSortProjectCommand(String arguments) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(arguments, PREFIX_DEADLINE);

        if (!arePrefixesPresent(argMultimap, PREFIX_DEADLINE)) {
            throw new ParseException(String.format(MESSAGE_MISSING_ARGUMENTS,
                    SortProjectCommand.MESSAGE_USAGE));
        }

        return new SortProjectCommand();
    }

    private ListProjectCommand parseListProjectCommand(String arguments) {
        return new ListProjectCommand();
    }

    private ProjectCommand parseSetProjectDefaultViewCommand(String arguments) {
        return new SetProjectDefaultViewCommand();
    }
}
