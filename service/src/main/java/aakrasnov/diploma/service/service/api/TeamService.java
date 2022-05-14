package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.common.TeamDto;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.dto.team.UpdateTeamInviteRsDto;
import java.util.Optional;

public interface TeamService {
    /**
     * Add a new team to the database.
     * @param teamDto Team which should be added
     * @return Added team.
     */
    TeamDto addTeam(TeamDto teamDto);

    /**
     * Update team by the specified id with passed dto.
     * For a successful update an author of the team for
     * update should be either a creator of the team or an admin.
     * @param id Id of the team for update
     * @param teamDto The team with updated info
     * @param user User identity
     * @return Result of operation (OK, BAD_REQUEST, FORBIDDEN) with optional message.
     */
    UpdateRsDto update(String id, TeamDto teamDto, User user);

    /**
     * Update invitation code of the specified team.
     * @param teamId Team id for which invitation code should be updated
     * @param user User identity
     * @return Team with a new invitation code if team exists and http status.
     */
    UpdateTeamInviteRsDto updateInvitationCode(String teamId, User user);

    /**
     * Obtain team by id.
     * @param teamId Id of the team which should be received
     * @return Team if it exists.
     */
    Optional<TeamDto> getById(String teamId);

    /**
     * Get team by invitation code.
     * @param invite Invitation code
     * @return Team if it exists.
     */
    Optional<TeamDto> getByInvitation(String invite);

    /**
     * Delete team by the specified id.
     * @param teamId Id of the team which should be deleted.
     * @param user User identity
     */
    void deleteById(String teamId, User user);
}
