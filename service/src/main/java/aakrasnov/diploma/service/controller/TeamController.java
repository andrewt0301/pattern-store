package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.TeamDto;
import aakrasnov.diploma.service.domain.Team;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.dto.team.UpdateTeamInviteRsDto;
import aakrasnov.diploma.service.service.api.TeamService;
import aakrasnov.diploma.service.service.api.UserService;
import aakrasnov.diploma.service.utils.PrincipalConverter;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${server.api}")
public class TeamController {
    private final TeamService teamService;

    private final UserService userService;

    public TeamController(final TeamService teamService, final UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping("auth/team/join/{code}")
    public ResponseEntity<HttpStatus> joinTeamByInvitationCode(
        Principal principal,
        @PathVariable("code") String code
    ) {
        User user = new PrincipalConverter(principal).toUser();
        Optional<TeamDto> team = teamService.getByInvitation(code);
        if (!team.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Set<Team> teams = user.getTeams();
        teams.add(Team.fromDto(team.get()));
        user.setTeams(teams);
        userService.updateUser(user.getId().toHexString(), user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("auth/team/{id}")
    public ResponseEntity<TeamDto> getById(
        Principal principal,
        @PathVariable("id") String id
    ) {
        return getTeamIfPermitted(principal, teamService.getById(id));
    }

    @GetMapping("auth/team/invite/{invite}")
    public ResponseEntity<TeamDto> getByInvitationCode(
        Principal principal,
        @PathVariable("invite") String invite
    ) {
        return getTeamIfPermitted(principal, teamService.getByInvitation(invite));
    }

    @PostMapping("auth/team/create")
    public ResponseEntity<TeamDto> addTeam(
        Principal principal,
        @RequestBody TeamDto teamDto
    ) {
        User user = new PrincipalConverter(principal).toUser();
        teamDto.setCreatorId(user.getId().toHexString());
        return new ResponseEntity<>(teamService.addTeam(teamDto), HttpStatus.CREATED);
    }

    @GetMapping("auth/team/{id}/update/invite")
    public ResponseEntity<TeamDto> updateInvitationCodeById(
        Principal principal,
        @PathVariable("id") String id
    ) {
        UpdateTeamInviteRsDto updRs = teamService.updateInvitationCode(
            id, new PrincipalConverter(principal).toUser()
        );
        if (!StringUtils.isEmpty(updRs.getMsg())) {
            log.error(updRs.getMsg());
        }
        return new ResponseEntity<>(updRs.getTeamDto(), HttpStatus.valueOf(updRs.getStatus()));
    }

    @GetMapping("auth/team/invite/{invite}/update/invite")
    public ResponseEntity<TeamDto> updateInvitationCodeByInvite(
        Principal principal,
        @PathVariable("invite") String invite
    ) {
        Optional<TeamDto> team = teamService.getByInvitation(invite);
        if (!team.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        UpdateTeamInviteRsDto updRs = teamService.updateInvitationCode(
            team.get().getId(), new PrincipalConverter(principal).toUser()
        );
        if (!StringUtils.isEmpty(updRs.getMsg())) {
            log.error(updRs.getMsg());
        }
        return new ResponseEntity<>(updRs.getTeamDto(), HttpStatus.valueOf(updRs.getStatus()));
    }

    @PostMapping("auth/team/{id}/update")
    public ResponseEntity<TeamDto> updateTeamById(
        Principal principal,
        @PathVariable("id") String id,
        @RequestBody TeamDto teamUpd
    ) {
        UpdateRsDto updRs = teamService.update(
            id,
            teamUpd,
            new PrincipalConverter(principal).toUser()
        );
        if (!StringUtils.isEmpty(updRs.getMsg())) {
            log.error(updRs.getMsg());
        }
        return new ResponseEntity<>(teamUpd, HttpStatus.valueOf(updRs.getStatus()));
    }

    @PostMapping("auth/team/invite/{invite}/update")
    public ResponseEntity<TeamDto> updateTeamByInvite(
        Principal principal,
        @PathVariable("invite") String invite,
        @RequestBody TeamDto teamUpd
    ) {
        Optional<TeamDto> team = teamService.getByInvitation(invite);
        if (!team.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        UpdateRsDto updRs = teamService.update(
            team.get().getId(),
            teamUpd,
            new PrincipalConverter(principal).toUser()
        );
        if (!StringUtils.isEmpty(updRs.getMsg())) {
            log.error(updRs.getMsg());
        }
        return new ResponseEntity<>(teamUpd, HttpStatus.valueOf(updRs.getStatus()));
    }

    @DeleteMapping("admin/team/{id}/delete")
    public ResponseEntity<HttpStatus> deleteTeamById(
        Principal principal,
        @PathVariable("id") String id
    ) {
        User user = new PrincipalConverter(principal).toUser();
        teamService.deleteById(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<TeamDto> getTeamIfPermitted(
        Principal principal, Optional<TeamDto> teamDto
    ) {
        User user = new PrincipalConverter(principal).toUser();
        if (!teamDto.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if (user.getTeams().contains(Team.fromDto(teamDto.get()))) {
            return new ResponseEntity<>(teamDto.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
