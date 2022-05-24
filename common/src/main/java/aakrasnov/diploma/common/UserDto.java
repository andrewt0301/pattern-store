package aakrasnov.diploma.common;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"id", "username", "role"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {
    private String id;

    private String username;

    private String password;

    private Role role;

    private Set<TeamDto> teams;

    private boolean isActive;
}
