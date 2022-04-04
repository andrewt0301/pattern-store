package aakrasnov.diploma.service.service;

import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.repo.UserRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepo userRepo, @Lazy final PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void doTmpActivity() {
        saveUser(
            new User("username", "pswd", Role.ADMIN)
        );
        final List<User> users = userRepo.findAll();
        System.out.println(users);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User saveUser(final User user) {
        final String pswd = user.getPassword();
        user.setPassword(passwordEncoder.encode(pswd));
        System.out.println(user.getPassword());
        return userRepo.save(user);
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
