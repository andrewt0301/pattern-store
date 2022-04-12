package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.filter.FilterByTeamId;
import aakrasnov.diploma.service.service.api.DocService;
import aakrasnov.diploma.service.utils.PrincipalConverter;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DocController {
    private final DocService docService;

    @Autowired
    public DocController(final DocService docService) {
        this.docService = docService;
    }

    @GetMapping("doc/{id}")
    public ResponseEntity<DocDto> getDocById(@PathVariable("id") String id) {
        // TODO: private docs. I think it is necessary to create set of teams.
        return docService.findById(id)
            .map(value -> ResponseEntity.status(HttpStatus.FOUND).body(value))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("docs/filtered")
    public ResponseEntity<List<DocDto>> docsCommonByFilters(
        @RequestBody List<Filter> filters
    ) {
        filters.add(FilterByTeamId.COMMON_TEAM_FILTER);
        return ResponseEntity.ok(docService.filteredDocuments(filters));
    }

    @PostMapping("auth/doc")
    public ResponseEntity<DocDto> addDoc(
        Principal principal,
        @RequestBody DocDto docDto
    ) {
        // TODO: check upload limits
//        User user = new PrincipalConverter(principal).toUser();
        return new ResponseEntity<>(docService.addDoc(docDto), HttpStatus.CREATED);
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
        return new ResponseEntity<>(updDoc, updRs.getStatus());
    }

    @GetMapping("auth/docs/team/{id}")
    public ResponseEntity<List<DocDto>> getDocByTeamId(
        Principal principal,
        @PathVariable("id") String id
    ) {
        User user = new PrincipalConverter(principal).toUser();
        if (user.getTeams().stream().noneMatch(team -> id.equals(team.getId()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(
            docService.filteredDocuments(Collections.singletonList(new FilterByTeamId(id))),
            HttpStatus.OK
        );
    }

    @PostMapping("auth/docs/filtered")
    public ResponseEntity<List<DocDto>> docsAuthByFilters(
        Principal principal,
        @RequestBody List<Filter> filters
    ) {
        User user = new PrincipalConverter(principal).toUser();
        user.getTeams().forEach(
            team -> filters.add(new FilterByTeamId(team.getId()))
        );
        filters.add(FilterByTeamId.COMMON_TEAM_FILTER);
        return ResponseEntity.ok(docService.filteredDocuments(filters));
    }

    @GetMapping("admin/doc/{id}/delete")
    public ResponseEntity<HttpStatus> deleteDocById(
        Principal principal,
        @PathVariable("id") String id
    ) {
        docService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("admin/docs")
    public List<DocDto> getDocs() {
        return docService.getAllDocs();
    }

}
