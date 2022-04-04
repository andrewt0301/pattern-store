package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.Doc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocRepo extends MongoRepository<Doc, String>, CustomDocRepo {
}
