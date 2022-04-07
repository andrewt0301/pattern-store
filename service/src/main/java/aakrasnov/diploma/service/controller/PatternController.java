package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.service.domain.Pattern;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.service.PatternServiceImpl;
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
    private final PatternServiceImpl ptrnService;

    @Autowired
    public PatternController(final PatternServiceImpl ptrnService) {
        this.ptrnService = ptrnService;
    }

    @PostMapping("auth/pattern")
    public ResponseEntity<Pattern> savePattern(@RequestBody Pattern pattern) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ptrnService.save(pattern));
    }
}
