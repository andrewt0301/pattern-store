package aakrasnov.diploma.client.cli;

import aakrasnov.diploma.client.api.BasicClientDocApi;
import aakrasnov.diploma.client.api.ClientDocApi;
import aakrasnov.diploma.client.api.cache.CacheIndexClientDoc;
import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.exception.BadInputDocFileException;
import aakrasnov.diploma.client.exception.ForbiddenOperationException;
import aakrasnov.diploma.client.exception.IncorrectCommandUsageException;
import aakrasnov.diploma.client.exception.InputFileNotFoundException;
import aakrasnov.diploma.client.exception.StatisticFileOutputException;
import aakrasnov.diploma.client.utils.PathConverter;
import aakrasnov.diploma.client.utils.RsAsGsonPretty;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.common.RsBaseDto;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
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

    @ArgGroup(exclusive = false)
    UpdateDocArg updateDocArg;

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

    static class FilterArg {
        @Option(
            names = {"--keyFilter"},
            required = true,
            description = "Key for filter. Option --filterMode should be passed."
        )
        private String key;

        @Option(
            names = {"--valueFilter", "--valFilter"},
            required = true,
            description = "Value for filter. Option --filterMode should be passed."
        )
        private String value;
    }

    static class UpdateDocArg {
        @Option(names = {"--updDocId"},
            required = true,
            description = "Id of document for updating. "
        )
        private String docId;

        @Option(names = {"--pathUpd"},
            required = true,
            description = "Path to the document for update. "
        )
        private String pathUpd;
    }

    @Option(names = {"--cache"},
        required = false,
        description = "This flag allows to make requests which use client cache."
    )
    private boolean isCache;

    @ArgGroup(exclusive = true)
    OnlyOne cmd;

    static class OnlyOne {
        @Option(names = {"--getById", "--docId"},
            required = true,
            description = "Id of document for obtaining. "
        )
        private String getDocId;

        @Option(names = {"--getByTeam", "--byTeam"},
            required = true,
            description = "Get documents by team id. User should be authenticated and " +
                          "have access to this team. "
        )
        private String getByTeamId;

        @Option(names = {"--deleteById"},
            required = true,
            description = "Id of document for removing. "
        )
        private String deleteDocId;

        @Option(names = {"--addDocFile"},
            required = true,
            description = "Path to the file with document for addition. "
        )
        private String addDocFile;

        @Option(names = {"--filterMode"},
            required = true,
            description = "This mode should be activated for passing filters' values. "
        )
        private boolean filterMode;

        @Option(names = {"--getAll"},
            required = true,
            description = "Get all documents from database. If you are not authenticated, " +
                          "only docs from common pool will be shown. If you are successfully " +
                          "authenticated, all available docs for your teams will be shown. "
        )
        private boolean getAll;

        @Option(names = {"--getByUser", "--byUser"},
            required = true,
            description = "Get documents by user. User should be authenticated. "
        )
        private boolean getByUser;

        @Option(names = {"--update"},
            required = true,
            description = "This mode should be activated for update of the documents. " +
                          "It is used together with specifying id of doc for update " +
                          "and path to the file with a new documents. " +
                          "User should be authenticated. "
        )
        private boolean updateMode;
    }

    @Option(
        names = {"--file"},
        description = "Path to the file with output results. If it is not used " +
                      "the results will be printed in the console."
    )
    private String outFile;

    @Option(
        names = {"--pretty"},
        description = "Convert got file with documents to string."
    )
    private boolean ispretty;

    private final ClientDocApi clientDoc;

    private final ClientDocApi cacheClientDoc;

    private Gson gson;

    public CliDoc() {
        this.clientDoc = new BasicClientDocApi(
            HttpClients.createDefault(), "http://localhost:8080"
        );
        this.cacheClientDoc = new CacheIndexClientDoc(clientDoc);
    }

    public CliDoc(final ClientDocApi clientDoc, final ClientDocApi cacheClientDoc) {
        this.clientDoc = clientDoc;
        this.cacheClientDoc = cacheClientDoc;
        gson = new Gson();
    }

    @Override
    public String call() {
        RsBaseDto res = new RsBaseDto();
        if (cmd == null) {
            throw new IncorrectCommandUsageException("You should select one obligatory command");
        }
        if (cmd.getDocId != null) {
            if (user != null) {
                User identity =  new User(user.username, user.password);
                if (isCache) {
                    res = cacheClientDoc.getDoc(cmd.getDocId, identity);
                } else {
                    res = clientDoc.getDoc(cmd.getDocId, identity);
                }
            } else {
                if (isCache) {
                    res = cacheClientDoc.getDocFromCommon(cmd.getDocId);
                } else {
                    res = clientDoc.getDocFromCommon(cmd.getDocId);
                }
            }
        }
        if (cmd.deleteDocId != null) {
            if (user == null) {
                throw new ForbiddenOperationException(
                    String.format("Failed to delete document '%s' for empty user", cmd.deleteDocId)
                );
            }
            User identity =  new User(user.username, user.password);
            if (isCache) {
                res = cacheClientDoc.deleteById(cmd.deleteDocId, identity);
            } else {
                res = clientDoc.deleteById(cmd.deleteDocId, identity);
            }
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
                DocDto toAdd = new PathConverter(Paths.get(cmd.addDocFile)).toDocDto();
                User identity =  new User(user.username, user.password);
                if (isCache) {
                    res = cacheClientDoc.add(toAdd, identity);
                } else {
                    res = clientDoc.add(toAdd, identity);
                }
            } catch (IOException exc) {
                throw new BadInputDocFileException(
                    String.format("Bad format of input file '%s' with doc", cmd.addDocFile)
                );
            }
        }
        if (cmd.filterMode) {
            if (filtersArg == null) {
                throw new IncorrectCommandUsageException("Please, specify filters");
            }
            List<Filter> filters = Arrays.stream(filtersArg).map(
                filter -> new Filter.Wrap(filter.key, filter.value)
            ).collect(Collectors.toList());
            if (user == null) {
                if (isCache) {
                    res = cacheClientDoc.filterDocsFromCommon(filters);
                } else {
                    res = clientDoc.filterDocsFromCommon(filters);
                }
            } else {
                User identity = new User(user.username, user.password);
                if (isCache) {
                    res = cacheClientDoc.filterDocuments(filters, identity);
                } else {
                    res = clientDoc.filterDocuments(filters, identity);
                }
            }
        }
        if (cmd.getAll) {
            if (user == null) {
                if (isCache) {
                    res = cacheClientDoc.getAllDocsFromCommon();
                } else {
                    res = clientDoc.getAllDocsFromCommon();
                }
            } else {
                User identity = new User(user.username, user.password);
                if (isCache) {
                    res = cacheClientDoc.getAllDocsForUser(identity);
                } else {
                    res = clientDoc.getAllDocsForUser(identity);
                }
            }
        }
        if (cmd.getByUser) {
            if (user == null) {
                throw new ForbiddenOperationException(
                    "It is necessary to be authenticated in order to get documents by the user."
                );
            }
            User identity = new User(user.username, user.password);
            if (isCache) {
                res = cacheClientDoc.getAllDocsForUser(identity);
            } else {
                res = clientDoc.getAllDocsForUser(identity);
            }
        }
        if (cmd.getByTeamId != null) {
            if (user == null) {
                throw new ForbiddenOperationException(
                    "It is necessary to be authenticated in order to get documents by the team."
                );
            }
            User identity = new User(user.username, user.password);
            if (isCache) {
                res = cacheClientDoc.getDocsByTeamId(cmd.getByTeamId, identity);
            } else {
                res = clientDoc.getDocsByTeamId(cmd.getByTeamId, identity);
            }
        }
        if (cmd.updateMode) {
            if (updateDocArg == null) {
                throw new IncorrectCommandUsageException("Please, specify info about doc update");
            }
            if (user == null) {
                throw new ForbiddenOperationException(
                    "It is necessary to be authenticated in order to perform update."
                );
            }
            if (!Files.exists(Paths.get(updateDocArg.pathUpd))) {
                throw new InputFileNotFoundException(
                    String.format("Failed to find file for update '%s'", updateDocArg.pathUpd)
                );
            }
            DocDto docDto;
            try {
                docDto = new PathConverter(Paths.get(updateDocArg.pathUpd)).toDocDto();
            } catch (IOException exc) {
                throw new BadInputDocFileException(
                    "Failed to convert update file to DocDto", exc
                );
            }
            User identity = new User(user.username, user.password);
            if (isCache) {
                res = cacheClientDoc.update(updateDocArg.docId, docDto, identity);
            } else {
                res = clientDoc.update(updateDocArg.docId, docDto, identity);
            }
        }
        String resText = new RsAsGsonPretty(res, gson).convert(ispretty);
        if (outFile != null) {
            Path path = Paths.get(outFile);
            try {
                Files.write(
                    path,
                    Collections.singleton(resText),
                    StandardOpenOption.CREATE
                );
                System.out.printf("Data was written to the file '%s'%n", outFile);
            } catch (IOException exc) {
                throw new StatisticFileOutputException(
                    String.format("Failed to output data to file '%s'", outFile),
                    exc
                );
            }
        } else {
            System.out.println("Result of command:");
            System.out.println(resText);
        }
        return res.toString();
    }
}
