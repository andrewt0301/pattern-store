package aakrasnov.diploma.service.repo.stata;

import aakrasnov.diploma.service.domain.StatisticDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticDocRepo extends MongoRepository<StatisticDoc, String>,
    CustomStatisticDocRepo {
}
