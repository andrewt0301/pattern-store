package aakrasnov.diploma.service.repo.stata;

import aakrasnov.diploma.service.domain.DocumentNames;
import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.dto.stata.DownloadDocDb;
import com.google.common.collect.Streams;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class StatisticDocRepoImpl implements CustomStatisticDocRepo {
    private final MongoDatabase storeDb;

    public StatisticDocRepoImpl(
        final MongoDatabase storeDb
    ) {
        this.storeDb = storeDb;
    }

    @Override
    public List<DownloadDocDb> getDownloadsCountForDocs(final Set<ObjectId> ids) {
        return Streams.stream(
            storeDb.getCollection(DocumentNames.STATISTIC_DOCS)
                .aggregate(
                    Arrays.asList(
                        Aggregates.match(Filters.in("documentId", new ArrayList<>(ids))),
                        Aggregates.group("$documentId", Accumulators.sum("count", 1))
                    )
                )
        ).map(Bson::toBsonDocument)
        .map(
            bson -> new DownloadDocDb(
                bson.getObjectId("_id").getValue().toHexString(),
                bson.getInt32("count").getValue()
            )
        ).collect(Collectors.toList());
    }

    @Override
    public List<StatisticDoc> getStatisticForDoc(final ObjectId docId) {
        return Streams.stream(
            storeDb.getCollection(DocumentNames.STATISTIC_DOCS)
                .find(Filters.eq("documentId", docId))
        ).map(Bson::toBsonDocument)
        .map(
            bson -> new StatisticDoc(
                bson.getObjectId("_id").getValue(),
                bson.getObjectId("documentId").getValue(),
                StatisticPtrns.fromJson(
                    bson.get("stataPtrns").asDocument().toJson()
                )
            )
        ).collect(Collectors.toList());
    }
}
