package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findById(String id);
}
