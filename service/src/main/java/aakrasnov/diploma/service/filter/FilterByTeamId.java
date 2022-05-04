package aakrasnov.diploma.service.filter;

import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.Team;

/**
 * Class for obtaining documents, which were created by the specified team.
 */
public class FilterByTeamId implements Filter {
    /**
     * Alias for common team filter.
     */
    public static Filter COMMON_TEAM_FILTER = new FilterByTeamId(Team.COMMON_TEAM_ID.toString());

    private final String teamId;

    public FilterByTeamId(final String teamId) {
        this.teamId = teamId;
    }

    @Override
    public String key() {
        return "team.id";
    }

    @Override
    public String value() {
        return teamId;
    }

    @Override
    public String toString() {
        return String.format(
            "FilterByTeamId(key=%s; value=%s)", key(), value()
        );
    }
}
