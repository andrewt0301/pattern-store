package aakrasnov.diploma.service.config;

import aakrasnov.diploma.service.repo.stata.StatisticDocRepo;
import aakrasnov.diploma.service.repo.stata.StatisticPtrnsRepo;
import aakrasnov.diploma.service.service.StatisticServiceImpl;
import aakrasnov.diploma.service.service.api.StatisticService;
import aakrasnov.diploma.service.utils.MyTmpTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticConfig {
    @Bean
    public StatisticDocRepo statisticDocRepo(ApplicationContext applicationContext) {
        return applicationContext.getBean(StatisticDocRepo.class);
    }

    @Bean
    public StatisticPtrnsRepo statisticPtrnsRepo(ApplicationContext applicationContext) {
        return applicationContext.getBean(StatisticPtrnsRepo.class);
    }

    @Bean
    public StatisticService statisticService(
        StatisticPtrnsRepo statisticPtrnsRepo,
        StatisticDocRepo statisticDocRepo
    ) {
        return new StatisticServiceImpl(
            statisticPtrnsRepo, statisticDocRepo
        );
    }

    @Bean
    public MyTmpTest myTmpTest(StatisticService statisticService) {
        return new MyTmpTest(statisticService);
    }
}
