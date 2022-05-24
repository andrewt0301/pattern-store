package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.Feedback;
import java.util.List;
import org.bson.types.ObjectId;

public interface CustomFeedbackRepo {
    List<Feedback> findFeedbackByDocId(ObjectId docId);
}
