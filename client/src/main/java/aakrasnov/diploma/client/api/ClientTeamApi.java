package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.team.AddTeamRsDto;
import aakrasnov.diploma.client.dto.team.DeleteTeamRsDto;
import aakrasnov.diploma.client.dto.team.TeamInfoRsDto;
import aakrasnov.diploma.client.dto.team.UpdateTeamRsDto;
import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.TeamDto;

public interface ClientTeamApi {
    /**
     * Join the team by invitation code.
     * @param invitation Team invitation code
     * @param user User identity
     * @return Http status of the operation.
     */
    RsBaseDto joinTeamByInvite(String invitation, User user);

    /**
     * Get a full description of the team by invitation code.
     * @param invite Team invitation code
     * @param user User identity
     * @return Team information and http status.
     */
    TeamInfoRsDto getTeamInfoByInvite(String invite, User user);

    /**
     * Get a full description of the team by team id.
     * @param id Team id
     * @param user User identity
     * @return Team information and http status.
     */
    TeamInfoRsDto getTeamInfoById(String id, User user);

    /**
     * Add a new team.
     * @param team Team for addition
     * @param user User identity
     * @return Added team and http status.
     */
    AddTeamRsDto add(TeamDto team, User user);

    /**
     * Update invitation code by team id.
     * @param id Team id
     * @param user User identity
     * @return Team with updated invitation code and http status.
     */
    TeamInfoRsDto updateInviteCodeById(String id, User user);

    /**
     * Update invitation code by team invitation code.
     * @param invite Team code invitation
     * @param user User identity
     * @return Team with updated invitation code and http status.
     */
    TeamInfoRsDto updateInviteCodeByInvite(String invite, User user);

    /**
     * Delete team by the specified team id.
     * @param id Team id
     * @param user User identity
     * @return Http status of the operation.
     */
    DeleteTeamRsDto deleteById(String id, User user);

    /**
     * Update existed team by the passed team for the specified id.
     * @param id Team id for update
     * @param teamUpd Team with info which will be saved
     * @param user User identity
     * @return Updated team.
     */
    UpdateTeamRsDto updateTeamById(String id, TeamDto teamUpd, User user);

    /**
     * Update existed team by the passed team for the specified invitation code.
     * @param invite Team code invitation
     * @param teamUpd Team with info which will be saved
     * @param user User identity
     * @return Updated team.
     */
    UpdateTeamRsDto updateTeamByInvite(String invite, TeamDto teamUpd, User user);
}
