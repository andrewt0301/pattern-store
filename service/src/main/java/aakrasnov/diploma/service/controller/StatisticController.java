package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.service.utils.MyTmpTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StatisticController {
    @Autowired
    private MyTmpTest myTmpTest;

    @GetMapping("statistic")
    public ResponseEntity<String> tmp() {
        myTmpTest.call();
        return ResponseEntity.ok("");
    }
}
