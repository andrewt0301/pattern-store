package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.AddUserRsDto;
import java.util.List;
import java.util.Optional;

public interface UserService {
    AddUserRsDto addUser(User user);

    List<User> getAll();

    Optional<User> findByUsername(String username);
}
