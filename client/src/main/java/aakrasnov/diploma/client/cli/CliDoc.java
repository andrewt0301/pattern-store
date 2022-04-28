package aakrasnov.diploma.client.cli;

import aakrasnov.diploma.client.api.BasicClientDocApi;
import aakrasnov.diploma.client.api.ClientDocApi;
import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.exception.BadInputDocFileException;
import aakrasnov.diploma.client.exception.ForbiddenOperationException;
import aakrasnov.diploma.client.exception.InputFileNotFoundException;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.common.RsBaseDto;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.apache.http.impl.client.HttpClients;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "document", aliases = {"doc"},
    description = "Command for interacting with documents from the database and local cache."
)
public class CliDoc implements Callable<String> {

    @ArgGroup(exclusive = false)
    UserArg user;

    @ArgGroup(exclusive = false)
    FilterArg[] filtersArg;

    static class UserArg {
        @Option(
            names = {"-u", "--username"},
            required = true,
            description = "Username for authentication"
        )
        private String username;

        @Option(
            names = {"-p", "--password"},
            required = true,
            description = "User password"
        )
        private String password;
    }

    static class FilterArg {
        @Option(
            names = {"--keyFilter", "--keyFltr"},
            required = true,
            description = "Key for filter. Option --filterMode should be passed."
        )
        private String key;

        @Option(
            names = {"--valueFilter", "--valueFlt", "--valFilter", "--valFltr"},
            required = true,
            description = "Value for filter. Option --filterMode should be passed."
        )
        private String value;
    }

    @ArgGroup(exclusive = true)
    OnlyOne cmd;

    static class OnlyOne {
        @Option(names = {"--getById"},
            required = true,
            description = "Id of document for obtaining"
        )
        private String getDocId;

        @Option(names = {"--deleteById"},
            required = true,
            description = "Id of document for removing"
        )
        private String deleteDocId;

        @Option(names = {"--addDocFile"},
            required = true,
            description = "Path to the file with document for addition"
        )
        private String addDocFile;

        @Option(names = {"--filterMode"},
            required = true,
            description = "This mode should activated for passing filters' values"
        )
        private boolean filterMode;

        @Option(names = {"--getAll"},
            required = true,
            description = "Get all documents from database. If you are not authenticated, " +
                          "only docs from common pool will be shown. If you are successfully " +
                          "authenticated, all available docs for your teams will be shown."
        )
        private boolean getAll;
    }

    private final ClientDocApi clientDoc;

    private Gson gson;

    public CliDoc() {
        this(new BasicClientDocApi(HttpClients.createDefault(), "http://localhost:8080"));
    }

    public CliDoc(final ClientDocApi clientDoc) {
        this.clientDoc = clientDoc;
        gson = new Gson();
    }

    @Override
    public String call() {
        RsBaseDto res = new RsBaseDto();
        if (cmd.getDocId != null) {
            if (user != null) {
                res = clientDoc.getDoc(cmd.getDocId, new User(user.username, user.password));
            } else {
                res = clientDoc.getDocFromCommon(cmd.getDocId);
            }
        }
        if (cmd.deleteDocId != null) {
            if (user == null) {
                throw new ForbiddenOperationException(
                    String.format("Failed to delete document '%s' for empty user", cmd.deleteDocId)
                );
            }
            res = clientDoc.deleteById(cmd.deleteDocId, new User(user.username, user.password));
        }
        if (cmd.addDocFile != null) {
            if (user == null) {
                throw new ForbiddenOperationException(
                    String.format(
                        "Failed to add from file document '%s' for empty user", cmd.addDocFile
                    )
                );
            }
            if (!Files.exists(Paths.get(cmd.addDocFile))) {
                throw new InputFileNotFoundException(
                    String.format("Failed to find file '%s' for doc uploading", cmd.addDocFile)
                );
            }
            try {
                res = clientDoc.add(
                    gson.fromJson(
                        new String(Files.readAllBytes(Paths.get(cmd.addDocFile))),
                        DocDto.class
                    ),
                    new User(user.username, user.password)
                );
            } catch (IOException exc) {
                throw new BadInputDocFileException(
                    String.format("Bad format of input file '%s' with doc", cmd.addDocFile)
                );
            }
        }
        if (cmd.filterMode && filtersArg != null) {
            List<Filter> filters = Arrays.stream(filtersArg).map(
                filter -> new Filter.Wrap(filter.key, filter.value)
            ).collect(Collectors.toList());
            if (user == null) {
                res = clientDoc.filterDocsFromCommon(filters);
            } else {
                res = clientDoc.filterDocuments(
                    filters,
                    new User(user.username, user.password)
                );
            }
        }
        if (cmd.getAll) {
            System.out.println("try later");
        }
        // TODO: getAllDocs, getDocsByTeamId, updateDoc
        return res.toString();
    }
}
