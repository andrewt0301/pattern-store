package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.dto.AddUserRsDto;
import aakrasnov.diploma.common.UserDto;

public interface ClientUserApi {
    /**
     * Add a new user.
     * @param userDto User for addition
     * @return Added user and http status.
     */
    AddUserRsDto add(UserDto userDto);
}
