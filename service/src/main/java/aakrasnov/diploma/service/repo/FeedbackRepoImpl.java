package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.service.domain.DocumentNames;
import aakrasnov.diploma.service.domain.Feedback;
import com.google.common.collect.Streams;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class FeedbackRepoImpl implements CustomFeedbackRepo {
    private final MongoDatabase storeDb;

    public FeedbackRepoImpl(final MongoDatabase storeDb) {
        this.storeDb = storeDb;
    }

    @Override
    public List<Feedback> findFeedbackByDocId(final ObjectId docId) {
        return Streams.stream(
            storeDb.getCollection(DocumentNames.FEEDBACK)
                .aggregate(
                    Collections.singletonList(
                        Aggregates.match(Filters.eq("docId", docId))
                    )
                )
        ).map(Bson::toBsonDocument)
            .map(
                bson -> {
                    Feedback feedback = new Feedback();
                    feedback.setId(bson.getObjectId("_id").getValue());
                    feedback.setUserId(bson.getObjectId("userId").getValue());
                    feedback.setDocId(bson.getObjectId("docId").getValue());
                    feedback.setText(bson.get("text").asString().getValue());
                    return feedback;
                }
            ).collect(Collectors.toList());
    }
}
