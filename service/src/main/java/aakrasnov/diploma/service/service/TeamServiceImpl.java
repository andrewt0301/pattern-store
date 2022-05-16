package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.TeamDto;
import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.Team;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.dto.team.UpdateTeamInviteRsDto;
import aakrasnov.diploma.service.repo.TeamRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.api.DocService;
import aakrasnov.diploma.service.service.api.TeamService;
import aakrasnov.diploma.service.utils.TeamInvite;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class TeamServiceImpl implements TeamService {
    private final TeamRepo teamRepo;

    private final DocService docService;

    private final UserRepo userRepo;

    public TeamServiceImpl(
        final TeamRepo teamRepo,
        final DocService docService,
        final UserRepo userRepo
    ) {
        this.teamRepo = teamRepo;
        this.docService = docService;
        this.userRepo = userRepo;
    }

    @Override
    public TeamDto addTeam(final TeamDto teamDto) {
        teamDto.setInvitation(UUID.randomUUID().toString());
        return Team.toDto(
            teamRepo.save(Team.fromDto(teamDto))
        );
    }

    @Override
    public UpdateRsDto update(final String id, final TeamDto teamDto, final User user) {
        Optional<Team> fromDb = teamRepo.findById(id);
        UpdateRsDto rs = new UpdateRsDto();
        if (!fromDb.isPresent()) {
            rs.setStatus(HttpStatus.NOT_FOUND.value());
            rs.setMsg(String.format("Team with id '%s' was not found", id));
            return rs;
        }
        boolean isCreator = user.getId().equals(fromDb.get().getCreatorId());
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        if (!isCreator && !isAdmin) {
            rs.setStatus(HttpStatus.FORBIDDEN.value());
            rs.setMsg("Operation is forbidden. You should be a creator or an admin");
            return rs;
        }
        teamDto.setId(id);
        if (StringUtils.isEmpty(teamDto.getInvitation())) {
            teamDto.setInvitation(fromDb.get().getInvitation());
        }
        if (!Objects.equals(fromDb.get().getInvitation(), teamDto.getInvitation())) {
            teamDto.setInvitation(UUID.randomUUID().toString());
        }
        Team saved = teamRepo.save(Team.fromDto(teamDto));
        teamDto.setId(saved.getId().toHexString());
        rs.setStatus(HttpStatus.OK.value());
        return rs;
    }

    @Override
    public UpdateTeamInviteRsDto updateInvitationCode(final String teamId, final User user) {
        UpdateTeamInviteRsDto rs = new UpdateTeamInviteRsDto();
        Optional<Team> team = teamRepo.findById(teamId);
        if (!team.isPresent()) {
            rs.setStatus(HttpStatus.NOT_FOUND.value());
            rs.setMsg(String.format("Team with id '%s' was not found", teamId));
            return rs;
        }
        if (!Role.ADMIN.equals(user.getRole()) && !user.getId().equals(team.get().getCreatorId())) {
            rs.setStatus(HttpStatus.FORBIDDEN.value());
            rs.setMsg("Operation is forbidden. You should be a creator or an admin");
            return rs;
        }
        rs.setTeamDto(
            Team.toDto(
                teamRepo.save(new TeamInvite(team.get()).updateInvite())
            )
        );
        rs.setStatus(HttpStatus.OK.value());
        return rs;
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
    public void deleteById(final String teamId, final User user) {
        Optional<Team> team = teamRepo.findById(teamId);
        if (!team.isPresent()) {
            return;
        }
        teamRepo.deleteById(teamId);
        docService.findByTeam(team.get()).stream()
            .peek(docDto -> docDto.setTeam(null))
            .forEach(docDto -> docService.update(docDto.getId(), docDto, user));
    }
}
