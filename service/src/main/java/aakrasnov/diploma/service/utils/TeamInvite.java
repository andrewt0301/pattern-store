package aakrasnov.diploma.service.utils;

import aakrasnov.diploma.service.domain.Team;
import java.util.UUID;

public class TeamInvite {
    private final Team team;

    public TeamInvite(final Team team) {
        this.team = team;
    }

    public Team updateInvite() {
        team.setInvitation(UUID.randomUUID().toString());
        return team;
    }
}
