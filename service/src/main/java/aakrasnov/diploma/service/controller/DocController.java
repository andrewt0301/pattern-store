package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.repo.DocRepo;
import aakrasnov.diploma.service.service.DocServiceImpl;
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
    private final DocServiceImpl docService;

    @Autowired
    public DocController(final DocServiceImpl docService) {
        this.docService = docService;
    }

    @GetMapping("doc/{id}")
    public ResponseEntity<DocDto> getDoc(@PathVariable("id") String id) {
        return docService.findById(id)
            .map(value -> ResponseEntity.status(HttpStatus.FOUND).body(value))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Autowired
    private DocRepo docRepo;

    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        System.out.println(docRepo.filteredDocuments(new Filter() {
            @Override
            public String key() {
                return "lang";
            }

            @Override
            public String value() {
                return "java";
            }
        }));
        return ResponseEntity.ok("hello");
    }

//    @GetMapping("docs")
//    public List<Doc> getDocs() {
//        return docService.findAll();
//    }

    @PostMapping("auth/doc")
    public ResponseEntity<DocDto> saveDoc(@RequestBody DocDto dto) {
//    public Doc saveDoc() {
//        Doc tmp = new Doc(
//            "java",
//            new Scenario(Scenario.Type.MIGRATION, new HashMap<>()),
//            new User("login", "pswd", Role.USER), // take from AuthenticationPrincipal
//            new ArrayList<>()
//    );
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(docService.addDoc(dto));
    }

}
