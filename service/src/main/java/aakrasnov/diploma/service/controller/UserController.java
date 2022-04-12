package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.AddUserRsDto;
import aakrasnov.diploma.service.service.api.UserService;
import com.google.gson.Gson;
import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("landing")
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
    public ResponseEntity<User> addUser(Principal principal, @RequestBody User user) {
        AddUserRsDto rs = userService.addUser(user);
        if (!StringUtils.isEmpty(rs.getMsg())) {
            log.warn(rs.getMsg());
            return new ResponseEntity<>(rs.getStatus());
        }
        return new ResponseEntity<>(rs.getUser(), HttpStatus.CREATED);
    }

    @GetMapping("admin/users")
    ResponseEntity<String> getUsers() {
        final Gson users = new Gson();
        return ResponseEntity.ok(users.toJson(userService.getAll()));
    }
}
