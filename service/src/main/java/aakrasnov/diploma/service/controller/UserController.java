package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.service.UserService;
import com.google.gson.Gson;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<String> landingPage(Principal principal) {
        Authentication authentication = (Authentication) principal;
        if (authentication == null) {
            return ResponseEntity.ok("here's a start page for unauthorized user");
        } else {
            User user = (User) authentication.getPrincipal();
            System.out.println(user);
            return ResponseEntity.ok("here's a start page for authorized user");
        }
    }

    @PostMapping("admin/user")
//    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<HttpStatus> addUser(Principal principal, @RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("user")
    ResponseEntity<String> tmp() {
        userService.doTmpActivity();
        return ResponseEntity.ok("it's okay");
    }

    @GetMapping("admin/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<String> getUsers() {
        final Gson users = new Gson();
        return ResponseEntity.ok(users.toJson(userService.getUsers()));
    }
}
