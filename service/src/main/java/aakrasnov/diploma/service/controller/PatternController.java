package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.service.domain.Pattern;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.service.PatternService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatternController {
    private final PatternService ptrnService;

    @Autowired
    public PatternController(final PatternService ptrnService) {
        this.ptrnService = ptrnService;
    }

    @PostMapping("auth/pattern")
    public ResponseEntity<Pattern> savePattern(@RequestBody Pattern pattern) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ptrnService.save(pattern));
    }

    @GetMapping("patterns")
    public ResponseEntity<List<Pattern>> getPatternsByAuthor(@RequestParam("id") User author) {
        return ResponseEntity.ok().body(ptrnService.findByAuthor(author));
    }
}
