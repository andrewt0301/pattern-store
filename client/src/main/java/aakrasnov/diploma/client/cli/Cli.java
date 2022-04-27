package aakrasnov.diploma.client.cli;

import picocli.CommandLine.Command;

@Command(name = "pattern-store", aliases = {"ptrn-store", "ptst"},
    description = "CLI for pattern-store client.  " +
                  "The list of available commands is below."
)
public class Cli implements Runnable {

    @Override
    public void run() {
    }
}
