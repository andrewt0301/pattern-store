package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.PatternDto;
import aakrasnov.diploma.service.domain.Pattern;
import aakrasnov.diploma.service.service.api.PatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatternController {
    private final PatternService ptrnService;

    @Autowired
    public PatternController(final PatternService patternService) {
        this.ptrnService = patternService;
    }

    @PostMapping("auth/pattern")
    public ResponseEntity<PatternDto> addPattern(@RequestBody Pattern pattern) {
        return new ResponseEntity<>(
            ptrnService.addPattern(Pattern.toDto(pattern)),
            HttpStatus.CREATED
        );
    }
}
