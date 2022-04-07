package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.DocRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.DocServiceImpl;
import aakrasnov.diploma.service.service.api.DocService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocServiceConfig {
    private final DocRepo docRepo;

    private final UserRepo userRepo;

    public DocServiceConfig(final DocRepo docRepo, final UserRepo userRepo) {
        this.docRepo = docRepo;
        this.userRepo = userRepo;
    }

    @Bean
    public DocService docService() {
        return new DocServiceImpl(docRepo, userRepo);
    }
}
