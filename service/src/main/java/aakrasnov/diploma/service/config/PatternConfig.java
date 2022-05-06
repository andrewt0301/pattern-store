package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.PatternRepo;
import aakrasnov.diploma.service.service.PatternServiceImpl;
import aakrasnov.diploma.service.service.api.PatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatternConfig {
    private final PatternRepo patternRepo;

    @Autowired
    public PatternConfig(final PatternRepo patternRepo) {
        this.patternRepo = patternRepo;
    }

    @Bean
    public PatternRepo patternRepo() {
        return patternRepo;
    }

    @Bean
    public PatternService patternService() {
        return new PatternServiceImpl(patternRepo);
    }
}
