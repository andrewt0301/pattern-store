package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.TeamRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.TeamServiceImpl;
import aakrasnov.diploma.service.service.api.TeamService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamConfig {
    private final TeamRepo teamRepo;

    private final UserRepo userRepo;

    public TeamConfig(final TeamRepo teamRepo, final UserRepo userRepo) {
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
    }

    @Bean
    public TeamRepo teamRepo() {
        return teamRepo;
    }

    @Bean
    public TeamService teamService(TeamRepo teamRepo) {
        return new TeamServiceImpl(teamRepo, userRepo);
    }
}
