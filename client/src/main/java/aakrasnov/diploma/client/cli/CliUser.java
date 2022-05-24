package aakrasnov.diploma.client.cli;

import aakrasnov.diploma.client.api.ClientUserApi;
import aakrasnov.diploma.client.api.ClientUserApiImpl;
import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.exception.IncorrectCommandUsageException;
import aakrasnov.diploma.common.Role;
import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.UserDto;
import java.util.concurrent.Callable;
import org.apache.http.impl.client.HttpClients;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "user",
    description = "Command for user registration."
)
public class CliUser implements Callable<String> {
    @ArgGroup(exclusive = false)
    UserArg user;

    static class UserArg {
        @Option(
            names = {"-u", "--username"},
            required = true,
            description = "Username for registration. "
        )
        private String username;

        @Option(
            names = {"-p", "--password"},
            required = true,
            description = "User password for registration. "
        )
        private String password;
    }

    private final ClientUserApi clientUser;

    public CliUser() {
        this(
            new ClientUserApiImpl(HttpClients.createDefault(), "http://localhost:8080")
        );
    }

    public CliUser(final ClientUserApi clientUser) {
        this.clientUser = clientUser;
    }

    @Override
    public String call() {
        RsBaseDto res;
        if (user == null) {
            throw new IncorrectCommandUsageException("Please, specify username and password");
        }
        UserDto toAdd = new UserDto();
        toAdd.setUsername(user.username);
        toAdd.setPassword(user.password);
        toAdd.setRole(Role.USER);
        res = clientUser.add(toAdd);
        return res.toString();
    }
}
