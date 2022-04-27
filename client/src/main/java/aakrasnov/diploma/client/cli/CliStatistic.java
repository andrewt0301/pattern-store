package aakrasnov.diploma.client.cli;

import aakrasnov.diploma.client.api.ClientStatisticApi;
import aakrasnov.diploma.client.exception.BadInputIdsFileException;
import aakrasnov.diploma.client.exception.PatternsIdFileNotFoundException;
import aakrasnov.diploma.client.exception.StatisticFileOutputException;
import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.stata.DocIdDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "statistic",
    aliases = {"stata"},
    description = "Command for obtaining statistic about usage of documents and patterns."
)
public class CliStatistic implements Callable<String> {
    @ArgGroup(exclusive = true, multiplicity = "1")
    private OnlyOne cmd;

    static class OnlyOne {
        @Option(names = {"--docId"}, required = true, description = "Id of document")
        private String doc;

        @Option(
            names = {"--sendStatisticFile", "--sendStataFile"},
            required = true,
            description = "Path to the file with statistic which would be sent. " +
                          "The content of this file should castable to the list of StatisticDto."
        )
        private String statisticFile;

        @Option(
            names = {"--patternsId", "--ptrnsId"},
            required = true,
            description = "Ids of patterns. You can specify multiple values. For example: " +
                          "... --patternsId ptrn_1 --patternsId ptrn_2 ..."
        )
        private String[] patterns;

        @Option(
            names = {"--patternsIdFile", "--ptrnsIdFile"},
            required = true,
            description = "Path to the file with patterns ids. Each id should be on a new line."
        )
        private String patternsFile;

        @Option(
            names = {"--downloadDocsId"},
            required = true,
            description = "Ids of docs for which it is necessary to calculate number " +
                          "of downloads. You can specify multiple values. For example: " +
                          "... --downloadDocsId doc_1 --downloadDocsId doc_2 ..."
        )
        private String[] downloadsDocs;

        @Option(
            names = {"--downloadDocsIdFile"},
            required = true,
            description = "Path to the file with doc ids. Each id should be on a new line."
        )
        private String downloadsDocsFile;
    }

    @Option(
        names = {"--merged", "--mergedStata"},
        description = "Merge results with statistics if this flag is specified."
    )
    private boolean merged;

    @Option(
        names = {"--file"},
        description = "Path to the file with output results. If it is not used " +
                      "the results will be printed in the console."
    )
    private String outFile;

    @Option(
        names = {"--appendFile"},
        description = "If the file by the specified path exists, this flag allows " +
                      "to append content to existed instead of creating a new one."
    )
    private boolean appendFile;

    @Option(
        names = {"--prettyString"},
        description = "Convert got file with statistic to string."
    )
    private boolean prettyString;

    private final ClientStatisticApi clientStatistic;

    private Gson gson;

    public CliStatistic(final ClientStatisticApi clientStatistic) {
        this.clientStatistic = clientStatistic;
        gson = new Gson();
    }

    @Override
    public String call() {
        RsBaseDto res = new RsBaseDto();
        if (cmd.statisticFile != null) {
            res = sendStatisticFile(cmd.statisticFile);
        }
        if (cmd.doc != null) {
            res = getByDocId(cmd.doc, merged);
        }
        if (cmd.patterns != null) {
            res = getByPatternsId(cmd.patterns, merged);
        }
        if (cmd.patternsFile != null) {
            res = getFromPatternsIdFile(cmd.patternsFile, merged);
        }
        if (cmd.downloadsDocs != null) {
            res = clientStatistic.getDownloadsCountForDocs(
                new HashSet<>(Arrays.asList(cmd.downloadsDocs))
            );
        }
        if (cmd.downloadsDocsFile != null) {
            res = clientStatistic.getDownloadsCountForDocs(
                getIdsFromFile(cmd.downloadsDocsFile)
            );
        }
        String resText = convertToPrettyIfNeed(res, prettyString);
        if (outFile != null) {
            Path path = Paths.get(outFile);
            try {
                if (Files.exists(path) && appendFile) {
                    Files.write(
                        path,
                        Collections.singleton(resText),
                        StandardOpenOption.APPEND
                    );
                    System.out.println("Used append option for data to output file");
                } else {
                    Files.write(
                        path,
                        Collections.singleton(resText),
                        StandardOpenOption.CREATE
                    );
                }
                System.out.printf("Data was written to the file '%s'%n", outFile);
            } catch (IOException exc) {
                throw new StatisticFileOutputException (
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

    private RsBaseDto sendStatisticFile(String file) {
        RsBaseDto res;
        Path source = Paths.get(file);
        if (!Files.exists(source)) {
            throw new PatternsIdFileNotFoundException(
                String.format("File '%s' does not exist", file)
            );
        }
        try {
            res = clientStatistic.sendDocStatistic(
                gson.fromJson(
                    new String(Files.readAllBytes(source)),
                    TypeToken.getParameterized(List.class, StatisticDto.class).getType()
                )
            );
        } catch (IOException exc) {
            throw new BadInputIdsFileException(
                String.format("Failed to read input with statistic '%s'", file),
                exc
            );
        }
        return res;
    }

    private RsBaseDto getByDocId(String docId, boolean isMerged) {
        RsBaseDto res;
        if (isMerged) {
            res = clientStatistic.getStatisticUsageMergedForDoc(new DocIdDto(docId));
        } else {
            res = clientStatistic.getStatisticForDoc(new DocIdDto(docId));
        }
        return res;
    }

    private RsBaseDto getByPatternsId(String[] patterns, boolean isMerged) {
        RsBaseDto res;
        Set<String> ids = new HashSet<>(Arrays.asList(patterns));
        if (isMerged) {
            res = clientStatistic.getStatisticMergedForPatterns(ids);
        } else {
            res = clientStatistic.getStatisticForPatterns(ids);
        }
        return res;
    }

    private RsBaseDto getFromPatternsIdFile(String patternsFile, boolean isMerged) {
        RsBaseDto res;
        Set<String> ids = getIdsFromFile(patternsFile);
        if (isMerged) {
            res = clientStatistic.getStatisticMergedForPatterns(ids);
        } else {
            res = clientStatistic.getStatisticForPatterns(ids);
        }
        return res;
    }

    private String convertToPrettyIfNeed(RsBaseDto res, boolean isPretty) {
        String resText;
        if (isPretty) {
            resText = new GsonBuilder().setPrettyPrinting()
                .create().toJson(res);
        } else {
            resText = gson.toJson(res);
        }
        return resText;
    }

    private static Set<String> getIdsFromFile(String file) {
        Path source = Paths.get(file);
        if (!Files.exists(source)) {
            throw new PatternsIdFileNotFoundException(
                String.format("File '%s' does not exist", file)
            );
        }
        Set<String> ids;
        try {
            ids = new HashSet<>(Files.readAllLines(source));
            ids.remove("");
        } catch (IOException exc) {
            throw new BadInputIdsFileException(
                String.format("Failed to read ids from '%s'", file),
                exc
            );
        }
        return ids;
    }
}
