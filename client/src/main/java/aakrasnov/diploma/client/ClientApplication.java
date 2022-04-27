package aakrasnov.diploma.client;

import aakrasnov.diploma.client.cli.Cli;
import aakrasnov.diploma.client.cli.CliDoc;
import aakrasnov.diploma.client.cli.CliStatistic;
import java.io.IOException;
import java.util.Arrays;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import picocli.CommandLine;

public class ClientApplication {
    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CommandLine cmd = new CommandLine(new Cli())
                .addSubcommand(new CommandLine.HelpCommand())
                .addSubcommand(new CliDoc())
                .addSubcommand(new CliStatistic(httpClient));
            cmd.setExecutionStrategy(new CommandLine.RunAll());
            cmd.execute(args);

            if (args.length == 0) {
                cmd.usage(System.out);
            }
        }
    }
}
