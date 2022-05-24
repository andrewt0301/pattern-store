package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.UserDto;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Data
@ToString(of = {"id", "username"})
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Document(DocumentNames.USERS)
public class User implements UserDetails {
    @Id
    private ObjectId id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private Role role;

    private Set<Team> teams = new HashSet<>();

    private boolean isActive = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(role);
    }

    public static User fromDto(UserDto dto) {
        User user = new User();
        if (dto.getId() != null) {
            user.setId(new ObjectId(dto.getId()));
        }
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(Role.valueOf(dto.getRole().name()));
        user.setTeams(
            Optional.ofNullable(dto.getTeams())
                .map(
                    teams -> teams.stream()
                    .map(Team::fromDto)
                    .collect(Collectors.toSet())
                ).orElse(new HashSet<>())
        );
        user.setActive(dto.isActive());
        return user;
    }

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        if (user.getId() != null) {
            dto.setId(user.getId().toHexString());
        }
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRole(aakrasnov.diploma.common.Role.valueOf(user.getRole().name()));
        dto.setTeams(
            Optional.ofNullable(user.getTeams())
                .map(
                    teams -> teams.stream()
                        .map(Team::toDto)
                        .collect(Collectors.toSet())
                ).orElse(new HashSet<>())
        );
        dto.setActive(user.isEnabled());
        return dto;
    }
}
