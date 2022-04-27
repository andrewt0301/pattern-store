package aakrasnov.diploma.client.cli;

import java.util.concurrent.Callable;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "statistic",
    aliases = {"stata"},
    description = "Command for obtaining statistic about usage of documents and patterns."
)
public class CliStatistic implements Callable<String> {

    @Option(names = {"--docId"}, description = "Id of document")
    private String docId;

    private final CloseableHttpClient httpClient;

    public CliStatistic() {
        httpClient = HttpClients.createDefault();
    }

    public CliStatistic(final CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String call() throws Exception {
        System.out.println("were here");
        System.out.println(docId);
        return null;
    }
}
