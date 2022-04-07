package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepo extends MongoRepository<Team, String> {
}
