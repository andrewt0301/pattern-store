package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.cache.CachedDocInfo;
import aakrasnov.diploma.client.test.DocDtoSample;
import aakrasnov.diploma.client.test.TestResource;
import aakrasnov.diploma.client.utils.TimeConverter;
import aakrasnov.diploma.common.DocDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndexFileJsonTest {
    static final String CACHE_DOC = "111122223333444455556666";

    private IndexFileJson index;

    @BeforeEach
    void setUp() {
        index = new IndexFileJson(
            new TestResource("cache-index.json").asInputStream()
        );
    }

    @Test
    void getCacheCreationTime() {
        MatcherAssert.assertThat(
            index.getIndexTimeCreation(),
            new IsEqual<>(LocalDateTime.parse("2022:05:03 19:08:54", DocDto.DATE_FORMATTER))
        );
    }

    @Test
    void getDocInfoWhenDocIsPresent() {
        Optional<CachedDocInfo> docInfo = index.getCachedDocInfo(CACHE_DOC);
        MatcherAssert.assertThat(
            docInfo.isPresent(),
            new IsEqual<>(true)
        );
        MatcherAssert.assertThat(
            docInfo.get().getDocId(),
            new IsEqual<>(CACHE_DOC)
        );
        MatcherAssert.assertThat(
            docInfo.get().getCachingTime(),
            new IsEqual<>("2022:05:03 19:10:17")
        );
    }

    @Test
    void getEmptyWhenDocIsAbsent() {
        MatcherAssert.assertThat(
            index.getCachedDocInfo("doc id absent").isPresent(),
            new IsEqual<>(false)
        );
    }

    @Test
    void doNothingWhenRemoveAbsentDoc() {
        index.removeDoc("some absent doc id");
        JsonObject after = new Gson().fromJson(index.asString(), JsonObject.class);
        MatcherAssert.assertThat(
            after.get(CACHE_DOC),
            new IsNot<>(new IsNull<>())
        );
        MatcherAssert.assertThat(
            after.get(IndexFileJson.CACHE_CREATION),
            new IsNot<>(new IsNull<>())
        );
    }

    @Test
    void addDocs() {
        String docId = "first";
        String timestamp = new TimeConverter(LocalDateTime.now()).asString();
        index.cacheDocs(
            Collections.singletonList(DocDtoSample.withIdAndTimestamp(docId, timestamp))
        );
        JsonObject after = new Gson().fromJson(index.asString(), JsonObject.class);
        MatcherAssert.assertThat(
            after.get(CACHE_DOC),
            new IsNot<>(new IsNull<>())
        );
        MatcherAssert.assertThat(
            after.get(docId).getAsJsonObject()
                .get(IndexFileJson.DOC_TIMESTAMP)
                .getAsString(),
            new IsEqual<>(timestamp)
        );
    }
}
