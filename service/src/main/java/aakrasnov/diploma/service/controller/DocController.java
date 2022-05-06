package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.Doc;
import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.Team;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.AddDocRsDto;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.filter.FilterByTeamId;
import aakrasnov.diploma.service.service.api.DocService;
import aakrasnov.diploma.service.utils.PrincipalConverter;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DocController {
    private final DocService docService;

    @Autowired
    public DocController(final DocService docService) {
        this.docService = docService;
    }

    @GetMapping("doc/{id}")
    public ResponseEntity<DocDto> getDocCommonById(@PathVariable("id") String id) {
        Optional<DocDto> docDto = docService.findById(id);
        return docDto.filter(doc -> doc.getTeam().getId().equals(Team.COMMON_TEAM_ID.toString()))
            .map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
            .orElseGet(() -> {
                if (docDto.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
                return ResponseEntity.notFound().build();
            });
    }

    @PostMapping("docs/filtered")
    public ResponseEntity<List<DocDto>> docsCommonByFilters(
        @RequestBody List<Filter> filters
    ) {
        filters.add(FilterByTeamId.COMMON_TEAM_FILTER);
        return ResponseEntity.ok(docService.filteredDocuments(filters));
    }

    @GetMapping("auth/doc/{id}")
    public ResponseEntity<DocDto> getDocById(
        Principal principal,
        @PathVariable("id") String id
    ) {
        User user = new PrincipalConverter(principal).toUser();
        // TODO: private docs. I think it is necessary to create set of teams who can access doc.
        Optional<DocDto> docDto = docService.findById(id);
        return docDto.filter(
            doc -> user.getRole().name().equals(Role.ADMIN.name())
                   || user.getTeams().stream().anyMatch(
                team -> doc.getTeam().getId().equals(team.getId().toHexString())
            )
        ).map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
        .orElseGet(() -> {
            if (docDto.isPresent()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.notFound().build();
        });
    }

    @PostMapping("auth/doc")
    public ResponseEntity<DocDto> addDoc(
        Principal principal,
        @RequestBody DocDto docDto
    ) {
        // TODO: check upload limits
        User user = new PrincipalConverter(principal).toUser();
        AddDocRsDto rs = docService.addDoc(docDto);
        if (!StringUtils.isEmpty(rs.getMsg())) {
            log.warn(rs.getMsg());
            return new ResponseEntity<>(HttpStatus.valueOf(rs.getStatus()));
        }
        return new ResponseEntity<>(Doc.toDto(rs.getDoc()), HttpStatus.CREATED);
    }

    @PostMapping("auth/doc/{id}/update")
    public ResponseEntity<DocDto> updateDoc(
        Principal principal,
        @PathVariable("id") String id,
        @RequestBody DocDto updDoc
    ) {
        User user = new PrincipalConverter(principal).toUser();
        UpdateRsDto updRs = docService.update(id, updDoc, user);
        if (!StringUtils.isEmpty(updRs.getMsg())) {
            log.error(updRs.getMsg());
        }
        return new ResponseEntity<>(updDoc, HttpStatus.valueOf(updRs.getStatus()));
    }

    @GetMapping("auth/docs/team/{id}")
    public ResponseEntity<List<DocDto>> getDocByTeamId(
        Principal principal,
        @PathVariable("id") String id
    ) {
        User user = new PrincipalConverter(principal).toUser();
        if (
            user.getTeams().stream()
                .noneMatch(team -> id.equals(team.getId().toHexString()))
        ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(
            docService.filteredDocuments(Collections.singletonList(new FilterByTeamId(id))),
            HttpStatus.OK
        );
    }

    @GetMapping("auth/docs/user")
    public ResponseEntity<List<DocDto>> getDocByUserId(
        Principal principal
    ) {
        User user = new PrincipalConverter(principal).toUser();
        final List<DocDto> docDtos;
        if (user.isAdmin()) {
            docDtos = docService.getAllDocs();
        } else {
            docDtos = docService.filteredDocuments(
                user.getTeams().stream()
                    .map(team -> new FilterByTeamId(team.getId().toHexString()))
                    .collect(Collectors.toList())
            );
        }
        return ResponseEntity.ok(docDtos);
    }

    @PostMapping("auth/docs/filtered")
    public ResponseEntity<List<DocDto>> docsByFilters(
        Principal principal,
        @RequestBody List<Filter> filters
    ) {
        User user = new PrincipalConverter(principal).toUser();
        user.getTeams().forEach(
            team -> filters.add(new FilterByTeamId(team.getId().toString()))
        );
        filters.add(FilterByTeamId.COMMON_TEAM_FILTER);
        return ResponseEntity.ok(docService.filteredDocuments(filters));
    }

    @DeleteMapping("admin/doc/{id}/delete")
    public ResponseEntity<HttpStatus> deleteDocById(
        Principal principal,
        @PathVariable("id") String id
    ) {
        docService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
