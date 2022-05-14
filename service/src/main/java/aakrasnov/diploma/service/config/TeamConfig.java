package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.TeamRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.TeamServiceImpl;
import aakrasnov.diploma.service.service.api.DocService;
import aakrasnov.diploma.service.service.api.TeamService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamConfig {
    @Bean
    public TeamRepo teamRepo(TeamRepo teamRepo) {
        return teamRepo;
    }

    @Bean
    public TeamService teamService(TeamRepo teamRepo, DocService docService, UserRepo userRepo) {
        return new TeamServiceImpl(teamRepo, docService, userRepo);
    }
}
