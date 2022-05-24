package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.UserDto;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.AddUserRsDto;
import aakrasnov.diploma.service.dto.UserAuthRsDto;
import aakrasnov.diploma.service.service.api.UserService;
import aakrasnov.diploma.service.utils.PrincipalConverter;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${server.api}")
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

    @PostMapping("auth/users/authenticate")
    ResponseEntity<UserAuthRsDto> authenticateUser(
        Principal principal
    ) {
        User user = new PrincipalConverter(principal).toUser();
        UserAuthRsDto rs = UserAuthRsDto.fromUser(user);
        rs.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(rs);
    }

    @PostMapping("user/add")
    public ResponseEntity<UserDto> addUser(
        @RequestBody UserDto user
    ) {
        AddUserRsDto rs = userService.addUser(user);
        if (rs.getUser() != null) {
            rs.getUser().setPassword("hidden");
        }
        if (!StringUtils.isEmpty(rs.getMsg())) {
            log.warn(rs.getMsg());
            return new ResponseEntity<>(HttpStatus.valueOf(rs.getStatus()));
        }
        return new ResponseEntity<>(User.toDto(rs.getUser()), HttpStatus.CREATED);
    }

    @GetMapping("admin/users")
    ResponseEntity<String> getUsers() {
        final Gson users = new Gson();
        return ResponseEntity.ok(users.toJson(userService.getAll()));
    }
}
