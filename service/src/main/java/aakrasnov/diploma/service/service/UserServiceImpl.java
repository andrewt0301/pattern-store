package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.UserDto;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.AddUserRsDto;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.api.UserService;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
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
    public AddUserRsDto addUser(final UserDto dto) {
        AddUserRsDto rs = new AddUserRsDto();
        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            rs.setStatus(HttpStatus.BAD_REQUEST.value());
            rs.setMsg(
                String.format("User with username '%s' already exists", dto.getUsername())
            );
            return rs;
        }
        final String pswd = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(pswd));
        // TODO: probably it would be better to activate manually
        dto.setActive(true);
        rs.setUser(userRepo.save(User.fromDto(dto)));
        return rs;
    }

    @Override
    public UpdateRsDto updateUser(final String id, final User userUpd) {
        Optional<User> user = userRepo.findById(id);
        UpdateRsDto res = new UpdateRsDto();
        if (!user.isPresent()) {
            res.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            userUpd.setId(new ObjectId(id));
            userRepo.save(userUpd);
        }
        res.setStatus(HttpStatus.OK.value());
        return res;
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
