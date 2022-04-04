package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.DocRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocRepoConfig {

    private final ApplicationContext applicationContext;

    @Autowired
    public DocRepoConfig(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public DocRepo docRepo() {
        return applicationContext.getBean(DocRepo.class);
    }
}
