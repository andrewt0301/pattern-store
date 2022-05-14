package aakrasnov.diploma.client;

import aakrasnov.diploma.client.api.BasicClientDocApi;
import aakrasnov.diploma.client.api.ClientDocApi;
import aakrasnov.diploma.client.api.ClientStatisticApi;
import aakrasnov.diploma.client.api.ClientStatisticApiImpl;
import aakrasnov.diploma.client.api.ClientTeamApi;
import aakrasnov.diploma.client.api.ClientTeamApiImpl;
import aakrasnov.diploma.client.api.cache.CacheIndexClientDoc;
import aakrasnov.diploma.client.cli.Cli;
import aakrasnov.diploma.client.cli.CliDoc;
import aakrasnov.diploma.client.cli.CliStatistic;
import aakrasnov.diploma.client.cli.CliTeam;
import java.io.IOException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import picocli.CommandLine;

public class ClientApplication {
    private static final String BASE = "http://localhost:8080/api";

    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ClientDocApi clientDoc = new BasicClientDocApi(httpClient, BASE);
            ClientStatisticApi clientStatistic = new ClientStatisticApiImpl(httpClient, BASE);
            ClientTeamApi clientTeam = new ClientTeamApiImpl(httpClient, BASE);
            // TODO: add autocomplete for CLI
            CommandLine cmd = new CommandLine(new Cli())
                .addSubcommand(new CommandLine.HelpCommand())
                .addSubcommand(new CliDoc(clientDoc, new CacheIndexClientDoc(clientDoc)))
                .addSubcommand(new CliStatistic(clientStatistic))
                .addSubcommand(new CliTeam(clientTeam));
            cmd.setExecutionStrategy(new CommandLine.RunAll());
            cmd.execute(args);
            if (args.length == 0) {
                cmd.usage(System.out);
            }
        }
    }
}
