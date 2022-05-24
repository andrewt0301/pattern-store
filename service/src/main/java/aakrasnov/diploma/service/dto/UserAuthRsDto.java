package aakrasnov.diploma.service.dto;

import aakrasnov.diploma.common.Role;
import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.service.domain.User;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAuthRsDto extends RsBaseDto implements Serializable {
    private String id;
    private String username;
    private String password;
    private Role role;

    public static UserAuthRsDto fromUser(User user) {
        UserAuthRsDto res = new UserAuthRsDto();
        res.setId(user.getId().toHexString());
        res.setUsername(user.getUsername());
        res.setPassword(user.getPassword());
        res.setRole(Role.valueOf(user.getRole().name()));
        return res;
    }
}
