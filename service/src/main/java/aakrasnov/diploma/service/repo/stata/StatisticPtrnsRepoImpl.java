package aakrasnov.diploma.service.repo.stata;

import aakrasnov.diploma.service.domain.DocumentNames;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.domain.Usage;
import com.google.common.collect.Streams;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class StatisticPtrnsRepoImpl implements CustomStatisticPtrnsRepo {
    private final MongoDatabase storeDb;

    public StatisticPtrnsRepoImpl(
        final MongoDatabase storeDb
    ) {
        this.storeDb = storeDb;
    }

    @Override
    public List<StatisticPtrns> getStatisticForPtrns(final Set<ObjectId> ptrnIds) {
        return Streams.stream(
            storeDb.getCollection(DocumentNames.STATISTIC_PTRNS)
                .find(
                    Filters.or(
                        Filters.in("success.patternId", ptrnIds),
                        Filters.in("failure.patternId", ptrnIds),
                        Filters.in("download.patternId", ptrnIds)
                    )
                )
        ).map(Bson::toBsonDocument)
            .map(
                bson -> {
                    BsonDocument bsonDoc = bson.asDocument();
                    StatisticPtrns stata = StatisticPtrns.fromJson(
                        bsonDoc.toJson()
                    );
                    stata.setId(bsonDoc.getObjectId("_id").getValue());
                    stata.setSuccess(filterUsage(stata.getSuccess(), ptrnIds));
                    stata.setFailure(filterUsage(stata.getFailure(), ptrnIds));
                    stata.setDownload(filterUsage(stata.getDownload(), ptrnIds));
                    return stata;
                }
            ).collect(Collectors.toList());
    }

    private static List<Usage> filterUsage(List<Usage> src, Set<ObjectId> ptrnIds) {
        return src.stream().filter(
            usage -> ptrnIds.contains(usage.getPatternId())
        ).collect(Collectors.toList());
    }
}
