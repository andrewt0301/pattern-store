package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.filter.FilterByTeamId;
import aakrasnov.diploma.service.service.api.DocService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocController {
    private final DocService docService;

    @Autowired
    public DocController(final DocService docService) {
        this.docService = docService;
    }

    @GetMapping("doc/{id}")
    public ResponseEntity<DocDto> getDocById(@PathVariable("id") String id) {
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
    public ResponseEntity<DocDto> addDoc(@RequestBody DocDto docDto) {
        // TODO: auth upload
        return new ResponseEntity<>(docService.addDoc(docDto), HttpStatus.CREATED);
    }

    @PostMapping("auth/doc/{id}/update")
    public ResponseEntity<DocDto> updateDoc(
        @PathVariable("id") String id,
        @RequestBody DocDto updDoc
    ) {
        // TODO: auth update. Get user from context
        String userId = "1";
        return new ResponseEntity<>(updDoc, docService.update(id, updDoc, userId));
    }

    @GetMapping("auth/docs/team/{id}")
    public ResponseEntity<List<DocDto>> getDocByTeamId(
        @PathVariable("id") String id
    ) {
        // TODO: auth get docs by team id.
        return new ResponseEntity<>(
            docService.filteredDocuments(Collections.singletonList(new FilterByTeamId(id))),
            HttpStatus.OK
        );
    }

    @PostMapping("auth/docs/filtered")
    public ResponseEntity<List<DocDto>> docsAuthByFilters(
        @RequestBody List<Filter> filters
    ) {
        filters.add(FilterByTeamId.COMMON_TEAM_FILTER);
        // TODO: add disjunction of filters for user's team
        // https://stackoverflow.com/questions/23137870/mongodb-check-if-field-is-one-of-many-values
        return ResponseEntity.ok(docService.filteredDocuments(filters));
    }

    @GetMapping("admin/doc/{id}/delete")
    public ResponseEntity<HttpStatus> deleteDocById(
        @PathVariable("id") String id
    ) {
        // TODO: auth delete (only for admin)
        docService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("admin/docs")
    public List<DocDto> getDocs() {
        return docService.getAllDocs();
    }

//    @Autowired
//    private DocRepo docRepo;
//
//    @GetMapping("hello")
//    public ResponseEntity<String> hello() {
//        System.out.println(docRepo.filteredDocuments(Collections.singletonList(new Filter() {
//            @Override
//            public String key() {
//                return "lang";
//            }
//
//            @Override
//            public String value() {
//                return "java";
//            }
//        })));
//        return ResponseEntity.ok("hello");
//    }
}
