package aakrasnov.diploma.service.service;

import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.repo.UserRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

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

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
            () -> {
                throw new UsernameNotFoundException(String.format("User '%s' not found", username));
            }
        );
    }
}
