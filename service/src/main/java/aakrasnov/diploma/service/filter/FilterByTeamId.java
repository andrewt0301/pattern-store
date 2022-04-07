package aakrasnov.diploma.service.filter;

import aakrasnov.diploma.common.Filter;

/**
 * Class for obtaining documents, which were created by the specified team.
 */
public class FilterByTeamId implements Filter {
    /**
     * Id team
     */
    private static final String COMMON_TEAM_ID = "1";

    /**
     * Alias for common team filter.
     */
    public static Filter COMMON_TEAM_FILTER = new FilterByTeamId(COMMON_TEAM_ID);

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
}
