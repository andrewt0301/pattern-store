package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.common.UserDto;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.AddUserRsDto;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import java.util.List;
import java.util.Optional;

public interface UserService {
    AddUserRsDto addUser(UserDto userDto);

    UpdateRsDto updateUser(String id, User userUpd);

    List<User> getAll();

    Optional<User> findByUsername(String username);
}
