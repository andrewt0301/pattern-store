package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.Team;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepo extends MongoRepository<Team, String> {
    Optional<Team> findByInvitation(String invitation);
}
