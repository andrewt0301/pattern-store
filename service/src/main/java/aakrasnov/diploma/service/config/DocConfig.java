package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.DocRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.DocServiceImpl;
import aakrasnov.diploma.service.service.api.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocConfig {

    private final ApplicationContext applicationContext;

    private final UserRepo userRepo;

    @Autowired
    public DocConfig(
        final ApplicationContext applicationContext,
        final UserRepo userRepo
    ) {
        this.applicationContext = applicationContext;
        this.userRepo = userRepo;
    }

    @Bean
    public DocRepo docRepo() {
        return applicationContext.getBean(DocRepo.class);
    }

    @Bean
    public DocService docService(DocRepo docRepo) {
        return new DocServiceImpl(docRepo, userRepo);
    }
}
