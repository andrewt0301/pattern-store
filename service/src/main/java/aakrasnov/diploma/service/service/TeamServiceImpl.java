package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.TeamDto;
import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.Team;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.repo.TeamRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.api.TeamService;
import aakrasnov.diploma.service.utils.TeamInvite;
import java.util.Optional;
import org.springframework.http.HttpStatus;

public class TeamServiceImpl implements TeamService {
    private final TeamRepo teamRepo;

    private final UserRepo userRepo;

    public TeamServiceImpl(final TeamRepo teamRepo, final UserRepo userRepo) {
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
    }

    @Override
    public TeamDto addTeam(final TeamDto teamDto) {
        return Team.toDto(
            teamRepo.insert(Team.fromDto(teamDto))
        );
    }

    @Override
    public UpdateRsDto update(final String id, final TeamDto teamDto, final String userId) {
        Optional<Team> fromDb = teamRepo.findById(id);
        UpdateRsDto rs = new UpdateRsDto();
        if (!fromDb.isPresent()) {
            rs.setStatus(HttpStatus.BAD_REQUEST);
            rs.setMsg(String.format("Team with id '%s' was not found", id));
            return rs;
        }
        Optional<User> user = userRepo.findById(userId);
        if (!user.isPresent()) {
            rs.setStatus(HttpStatus.FORBIDDEN);
            rs.setMsg(String.format("Not found user with id '%s'", userId));
            return rs;
        }
        boolean isCreator = user.get().getId().equals(fromDb.get().getCreatorId());
        boolean isAdmin = user.get().getRole().equals(Role.ADMIN);
        if (!isCreator && !isAdmin) {
            rs.setStatus(HttpStatus.FORBIDDEN);
            rs.setMsg("Operation is forbidden. You should be a creator or an admin");
            return rs;
        }
        teamDto.setId(id);
        Team saved = teamRepo.save(Team.fromDto(teamDto));
        teamDto.setId(saved.getId());
        rs.setStatus(HttpStatus.OK);
        return rs;
    }

    @Override
    public Optional<TeamDto> updateInvitationCode(final String teamId) {
        return teamRepo.findById(teamId).map(
            team -> teamRepo.insert(new TeamInvite(team).updateInvite())
        ).map(Team::toDto);
    }

    @Override
    public Optional<TeamDto> getById(final String teamId) {
        return teamRepo.findById(teamId).map(Team::toDto);
    }

    @Override
    public Optional<TeamDto> getByInvitation(final String invite) {
        return teamRepo.findByInvitation(invite).map(Team::toDto);
    }

    @Override
    public void deleteById(final String teamId) {
        teamRepo.deleteById(teamId);
    }
}
