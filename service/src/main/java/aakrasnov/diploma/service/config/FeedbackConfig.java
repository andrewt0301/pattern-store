package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.FeedbackRepo;
import aakrasnov.diploma.service.service.FeedbackServiceImpl;
import aakrasnov.diploma.service.service.api.FeedbackService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedbackConfig {
    @Bean
    public FeedbackRepo feedbackRepo(FeedbackRepo feedbackRepo) {
        return feedbackRepo;
    }

    @Bean
    public FeedbackService feedbackService(FeedbackRepo feedbackRepo) {
        return new FeedbackServiceImpl(feedbackRepo);
    }
}
