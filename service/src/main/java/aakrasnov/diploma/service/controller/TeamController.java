package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.TeamDto;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.service.api.TeamService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TeamController {
    private final TeamService teamService;

    public TeamController(final TeamService teamService) {
        this.teamService = teamService;
    }


    @GetMapping("team/invitation/{code}")
    public ResponseEntity<TeamDto> getByInvitationCode(
        @PathVariable("code") String code
    ) {
        return teamService.getByInvitation(code)
            .map(ResponseEntity::ok)
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("auth/team/{id}")
    public ResponseEntity<TeamDto> getById(
        @PathVariable("id") String id
    ) {
        return teamService.getById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("auth/team/create")
    public ResponseEntity<TeamDto> addTeam(@RequestBody TeamDto teamDto) {
        return new ResponseEntity<>(teamService.addTeam(teamDto), HttpStatus.CREATED);
    }

    @PostMapping("auth/team/{id}/update/invite")
    public ResponseEntity<TeamDto> updateInvitationCode(
        @PathVariable("id") String id
    ) {
        Optional<TeamDto> team = teamService.updateInvitationCode(id);
        return team.map(ResponseEntity::ok)
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("auth/team/{id}/update")
    public ResponseEntity<TeamDto> updateTeam(
        @PathVariable("id") String id,
        @RequestBody TeamDto teamUpd
    ) {
        // TODO: auth update. Get user from context
        String userId = "1";
        UpdateRsDto updRs = teamService.update(id, teamUpd, userId);
        if (!StringUtils.isEmpty(updRs.getMsg())) {
            log.error(updRs.getMsg());
        }
        return new ResponseEntity<>(teamUpd, updRs.getStatus());
    }

    @GetMapping("admin/team/{id}/delete")
    public ResponseEntity<HttpStatus> deleteTeamById(
        @PathVariable("id") String id
    ) {
        // TODO: auth delete (only for admin)
        teamService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
