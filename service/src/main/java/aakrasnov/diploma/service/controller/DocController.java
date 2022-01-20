package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.service.domain.Doc;
import aakrasnov.diploma.service.service.DocService;
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
    private DocService docService;

    @Autowired
    public DocController(final DocService docService) {
        this.docService = docService;
    }

    @GetMapping("doc/{id}")
    public ResponseEntity<Doc> getDoc(@PathVariable("id") String id) {
        return docService.getById(id)
            .map(value -> ResponseEntity.status(HttpStatus.FOUND).body(value))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("docs")
    public List<Doc> getDocs() {
        return docService.findAll();
    }

    @PostMapping("doc")
    public ResponseEntity<Doc> saveDoc(@RequestBody Doc doc) {
//    public Doc saveDoc() {
//        Doc tmp = new Doc(
//            "java",
//            new Scenario(Scenario.Type.MIGRATION, new HashMap<>()),
//            new User("login", "pswd", Role.USER), // take from AuthenticationPrincipal
//            new ArrayList<>()
//    );
        return ResponseEntity.status(HttpStatus.CREATED).body(docService.save(doc));
    }

}
