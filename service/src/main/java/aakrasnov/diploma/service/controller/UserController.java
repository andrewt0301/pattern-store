package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<String> landingPage() {
        return ResponseEntity.ok("here's a start page");
    }

    @GetMapping("user")
    ResponseEntity<String> tmp() {
        userService.doTmpActivity();
        return ResponseEntity.ok("it's okay");
    }

}
