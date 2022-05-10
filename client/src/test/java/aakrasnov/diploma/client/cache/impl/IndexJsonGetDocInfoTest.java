package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.cache.CachedDocInfo;
import aakrasnov.diploma.client.test.TestResource;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

public class IndexJsonGetDocInfoTest {
    @Test
    void getCachedDocInfoWhenIndexExist() {
        Optional<CachedDocInfo> docInfo = new IndexJson(
            new TestResource("index.json").asPath()
        ).getCachedDocInfo(IndexFileJsonTest.CACHE_DOC);
        MatcherAssert.assertThat(
            docInfo.isPresent(),
            new IsEqual<>(true)
        );
        MatcherAssert.assertThat(
            docInfo.get(),
            new IsEqual<>(
                CachedDocInfo.builder()
                    .docId("111122223333444455556666")
                    .docTimestamp("2022:03:22 11:01:10")
                    .path("pathToCachedFileAndDocId.json")
                    .cachingTime("2022:05:03 19:10:17")
                    .build()
            )
        );
    }

    @Test
    void getCachedDocInfoWhenIndexNotExist() {
        MatcherAssert.assertThat(
            new IndexJson(Paths.get("not", "exist", "index1245.json"))
                .getCachedDocInfo("anyDocId").isPresent(),
            new IsEqual<>(false)
        );
    }

    @Test
    void getCachedDocInfoSimultaneously() {
        int count = 100;
        CountDownLatch latch = new CountDownLatch(
            Math.min(count, Runtime.getRuntime().availableProcessors() - 1)
        );
        AtomicReferenceArray<Boolean> infosExist = new AtomicReferenceArray<>(count);
        List<CompletableFuture<Boolean>> rqs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int tmpI = i;
            rqs.add(
                CompletableFuture.runAsync(
                    () -> {
                        latch.countDown();
                        try {
                            latch.await();
                        } catch (final InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            throw new IllegalStateException(ex);
                        }
                    }
                ).thenApply(
                    noth -> new TestResource("index.json").asPath()
                ).thenApply(IndexJson::new)
                    .thenApply(index -> index.getCachedDocInfo(IndexFileJsonTest.CACHE_DOC))
                    .thenApply(docInfo -> infosExist.getAndSet(tmpI, docInfo.isPresent()))
            );
        }
        CompletableFuture.allOf(rqs.toArray(new CompletableFuture[0])).join();
        for (int i = 0; i < count; i++) {
            MatcherAssert.assertThat(
                infosExist.get(i),
                new IsEqual<>(true)
            );
        }
    }
}
