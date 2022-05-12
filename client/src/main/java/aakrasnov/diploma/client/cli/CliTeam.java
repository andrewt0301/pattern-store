package aakrasnov.diploma.client.cli;

import picocli.CommandLine;

@CommandLine.Command(
    name = "team",
    description = "Command for interacting with teams which give access to the documents."
)
public class CliTeam {
    // Commands which should be implemented:
    // Admin create team
    // Enter to the team by invite code (should be authenticated).
    // Refresh invite code. A new one should be generated
    // Update team of a specific doc
    // View invite code. If you are creator (maybe also if you are in the team).
    // Admin delete team (this team should be removed from all users).
    // Update team information
}
