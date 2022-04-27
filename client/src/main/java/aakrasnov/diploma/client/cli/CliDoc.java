package aakrasnov.diploma.client.cli;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(
    name = "document", aliases = {"doc"},
    description = "Command for interacting with documents from the database and local cache."
)
public class CliDoc implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("were in cliDoc");
        return null;
    }
}
