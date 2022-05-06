package aakrasnov.diploma.service.service;

import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.AddUserRsDto;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.api.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepo userRepo, final PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AddUserRsDto addUser(final User user) {
        AddUserRsDto rs = new AddUserRsDto();
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            rs.setStatus(HttpStatus.BAD_REQUEST.value());
            rs.setMsg(
                String.format("User with username '%s' already exists", user.getUsername())
            );
            return rs;
        }
        final String pswd = user.getPassword();
        user.setPassword(passwordEncoder.encode(pswd));
        // TODO: probably it would be better to activate manually
        user.setActive(true);
        rs.setUser(userRepo.save(user));
        return rs;
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }
}
