package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.Pattern;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatternRepo extends MongoRepository<Pattern, String> {
    List<Pattern> findPatternByAuthorId(String authorId);
}
