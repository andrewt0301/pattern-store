package aakrasnov.diploma.service.repo.stata;

import aakrasnov.diploma.service.domain.StatisticPtrns;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticPtrnsRepo extends MongoRepository<StatisticPtrns, String>,
    CustomStatisticPtrnsRepo {
}
