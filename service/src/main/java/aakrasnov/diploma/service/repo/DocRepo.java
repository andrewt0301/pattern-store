package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.Doc;
import aakrasnov.diploma.service.domain.Team;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocRepo extends MongoRepository<Doc, String>, CustomDocRepo {
    List<Doc> findByTeam(Team team);
}
