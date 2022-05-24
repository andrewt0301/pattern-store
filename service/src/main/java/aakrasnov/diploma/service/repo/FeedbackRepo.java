package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepo extends MongoRepository<Feedback, String>, CustomFeedbackRepo {
}
