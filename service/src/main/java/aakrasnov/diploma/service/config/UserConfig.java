package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.DbUserDetailsService;
import aakrasnov.diploma.service.service.UserServiceImpl;
import aakrasnov.diploma.service.service.api.TeamService;
import aakrasnov.diploma.service.service.api.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public UserService userService(
        UserRepo userRepo,
        TeamService teamService,
        PasswordEncoder passwordEncoder
    ) {
        return new UserServiceImpl(userRepo, teamService, passwordEncoder);
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new DbUserDetailsService(userService);
    }
}
