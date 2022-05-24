package aakrasnov.diploma.client.cli;

import aakrasnov.diploma.client.api.ClientTeamApi;
import aakrasnov.diploma.client.api.ClientTeamApiImpl;
import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.exception.BadInputFileException;
import aakrasnov.diploma.client.exception.IncorrectCommandUsageException;
import aakrasnov.diploma.client.exception.InputFileNotFoundException;
import aakrasnov.diploma.client.utils.PathConverter;
import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.TeamDto;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.HttpClients;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "team",
    description = "Command for interacting with teams which give access to the documents."
)
public class CliTeam implements Callable<String> {
    @ArgGroup(exclusive = false)
    UserArg user;

    @ArgGroup(exclusive = true)
    OnlyOne cmd;

    @ArgGroup(exclusive = false)
    UpdateTeamArg updateTeamArg;

    static class UserArg {
        @Option(
            names = {"-u", "--username"},
            required = true,
            description = "Username for authentication. "
        )
        private String username;

        @Option(
            names = {"-p", "--password"},
            required = true,
            description = "User password. "
        )
        private String password;
    }

    static class UpdateTeamArg {
        @ArgGroup(exclusive = true)
        KeyUpdArg key;

        @Option(names = {"--pathUpd"},
            required = true,
            description = "Path to the team for update. "
        )
        private String pathUpd;

        static class KeyUpdArg {
            @Option(names = {"--updTeamId"},
                required = true,
                description = "Id of team for updating. "
            )
            private String teamId;

            @Option(names = {"--updTeamInvite"},
                required = true,
                description = "Invitation code of team for updating. "
            )
            private String teamInvite;
        }
    }

    static class OnlyOne {
        @Option(names = {"--getById", "--id"},
            required = true,
            description = "Id of team for obtaining. "
        )
        private String getTeamId;

        @Option(names = {"--getByInvite", "--invite"},
            required = true,
            description = "Invitation code of team for obtaining. "
        )
        private String getTeamInvite;

        @Option(names = {"--joinByInvite"},
            required = true,
            description = "Invitation code for joining a user to the team. "
        )
        private String joinInvite;

        @Option(names = {"--addTeam"},
            required = true,
            description = "Path to the file with team for addition. "
        )
        private String addTeamFile;

        @Option(names = {"--updInviteById"},
            required = true,
            description = "Id of team for invitation code updating. "
        )
        private String updInviteById;

        @Option(names = {"--updInviteByInvite"},
            required = true,
            description = "Invitation code of team for invitation code updating. "
        )
        private String updInviteByInvite;

        @Option(names = {"--deleteId"},
            required = true,
            description = "Id of team for team removing. "
        )
        private String deleteId;

        @Option(names = {"--update"},
            required = true,
            description = "This mode should be activated for update of the team. " +
                          "It is used together with specifying id or invite of team " +
                          "for update and path to the file with a new team."
        )
        private boolean updateMode;
    }

    private final ClientTeamApi clientTeam;

    public CliTeam() {
        this(
            new ClientTeamApiImpl(HttpClients.createDefault(), "http://localhost:8080")
        );
    }

    public CliTeam(final ClientTeamApi clientTeam) {
        this.clientTeam = clientTeam;
    }

    @Override
    public String call() {
        RsBaseDto res = new RsBaseDto();
        if (cmd == null) {
            throw new IncorrectCommandUsageException("You should select one obligatory command");
        }
        if (user == null) {
            throw new IncorrectCommandUsageException("Please, specify username and login");
        }
        User identity = new User(user.username, user.password);
        if (cmd.joinInvite != null) {
            res = clientTeam.joinTeamByInvite(cmd.joinInvite, identity);
        }
        if (cmd.getTeamId != null) {
            res = clientTeam.getTeamInfoById(cmd.getTeamId, identity);
        }
        if (cmd.getTeamInvite != null) {
            res = clientTeam.getTeamInfoByInvite(cmd.getTeamInvite, identity);
        }
        if (cmd.addTeamFile != null) {
            res = addTeamFromFile(identity);
        }
        if (cmd.updInviteById != null) {
            res = clientTeam.updateInviteCodeById(cmd.updInviteById, identity);
        }
        if (cmd.updInviteByInvite != null) {
            res = clientTeam.updateInviteCodeByInvite(cmd.updInviteByInvite, identity);
        }
        if (cmd.deleteId != null) {
            res = clientTeam.deleteById(cmd.deleteId, identity);
        }
        if (cmd.updateMode) {
            res = updateTeamFromFile(identity);
        }
        return res.toString();
    }

    private RsBaseDto updateTeamFromFile(User identity) {
        RsBaseDto res;
        if (updateTeamArg == null || updateTeamArg.key == null) {
            throw new IncorrectCommandUsageException("Please, specify info about team update");
        }
        if (!Files.exists(Paths.get(updateTeamArg.pathUpd))) {
            throw new InputFileNotFoundException(
                String.format("Failed to find file for update '%s'", updateTeamArg.pathUpd)
            );
        }
        TeamDto teamDto;
        try {
            teamDto = new PathConverter(Paths.get(updateTeamArg.pathUpd)).toTeamDto();
        } catch (IOException exc) {
            throw new BadInputFileException("Failed to convert update file to TeamDto", exc);
        }
        if (updateTeamArg.key.teamId != null) {
            res = clientTeam.updateTeamById(updateTeamArg.key.teamId, teamDto, identity);
        } else if (updateTeamArg.key.teamInvite != null) {
            res = clientTeam.updateTeamByInvite(updateTeamArg.key.teamInvite, teamDto, identity);
        } else {
            res = new RsBaseDto();
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    private RsBaseDto addTeamFromFile(User identity) {
        if (!Files.exists(Paths.get(cmd.addTeamFile))) {
            throw new InputFileNotFoundException(
                String.format("Failed to find file '%s' for team creating", cmd.addTeamFile)
            );
        }
        try {
            TeamDto toAdd = new PathConverter(Paths.get(cmd.addTeamFile)).toTeamDto();
            return clientTeam.add(toAdd, identity);
        } catch (IOException exc) {
            throw new BadInputFileException(
                String.format("Bad format of input file '%s' with team", cmd.addTeamFile)
            );
        }
    }
}
