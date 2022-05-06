package aakrasnov.diploma.service.service;

import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.service.api.UserService;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DbUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public DbUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(
                String.format("Not found user for username '%s'", username)
            );
        }
        return user.get();
    }
}
