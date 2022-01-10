package aakrasnov.diploma.service.service;

import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.repo.UserRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepo userRepo;

    @Autowired
    public UserService(final UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void doTmpActivity() {
        userRepo.insert(
            new User("login", "pswd", Role.ADMIN)
        );
        final List<User> users = userRepo.findAll();
        System.out.println(users);
    }
}
