package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.cache.CachedDocInfo;
import aakrasnov.diploma.client.test.DocDtoSample;
import aakrasnov.diploma.client.test.PathConverter;
import aakrasnov.diploma.client.test.TestResource;
import aakrasnov.diploma.common.DocDto;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;

class IndexJsonTest {
    static final String DOC_1_ID = "123abcde123abcde123abcde";

    @Test
    void getCachedDocInfoWhenIndexExist() {
        Optional<CachedDocInfo> docInfo = new IndexJson(
            new TestResource("cache-index.json").asPath()
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
                    .path("pathToCachedFile.json")
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
                    noth -> new TestResource("cache-index.json").asPath()
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

    @Test
    void invalidateExistingDocInIndexFile() throws IOException {
        String tmpIndex = "tmp-index.json";
        Path tmpIndexPath = Files.createTempFile("path", tmpIndex);
        Files.write(tmpIndexPath, new TestResource("cache-index.json").asBytes());
        MatcherAssert.assertThat(
            new PathConverter(tmpIndexPath).toJsonObj().get(IndexFileJsonTest.CACHE_DOC),
            new IsNot<>(new IsNull<>())
        );
        new IndexJson(tmpIndexPath)
            .invalidateByDocId(IndexFileJsonTest.CACHE_DOC);
        MatcherAssert.assertThat(
            new PathConverter(tmpIndexPath).toJsonObj().get(IndexFileJsonTest.CACHE_DOC),
            new IsNull<>()
        );
    }

    @Test
    void invalidateNotExistingDocInIndexFile() throws IOException {
        String tmpIndex = "tmp-index.json";
        String absentDocId = "absent document id";
        Path tmpIndexPath = Files.createTempFile("path", tmpIndex);
        Files.write(tmpIndexPath, new TestResource("cache-index.json").asBytes());
        MatcherAssert.assertThat(
            new PathConverter(tmpIndexPath).toJsonObj().get(absentDocId),
            new IsNull<>()
        );
        new IndexJson(tmpIndexPath).invalidateByDocId(absentDocId);
        JsonObject after = new PathConverter(tmpIndexPath).toJsonObj();
        MatcherAssert.assertThat(
            after.get(absentDocId),
            new IsNull<>()
        );
        MatcherAssert.assertThat(
            after.get(IndexFileJsonTest.CACHE_DOC),
            new IsNot<>(new IsNull<>())
        );
    }

    @Test
    void cacheDocsWhenIndexFileExist() throws IOException {
        Path indexPath = new TestResource("cache-index.json").asPath();
        new IndexJson(indexPath).cacheDocs(docsDto(DOC_1_ID));
        JsonObject after = new PathConverter(indexPath).toJsonObj();
        MatcherAssert.assertThat(
            after.get(DOC_1_ID),
            new IsNot<>(new IsNull<>())
        );
        MatcherAssert.assertThat(
            after.get(IndexFileJsonTest.CACHE_DOC),
            new IsNot<>(new IsNull<>())
        );
    }

    @Test
    void cacheDocsSimultaneously() throws IOException {
        int count = 100;
        CountDownLatch latch = new CountDownLatch(
            Math.min(count, Runtime.getRuntime().availableProcessors() - 1)
        );
        Path indexPath = new TestResource("cache-index.json").asPath();
        AtomicReferenceArray<String> docsIds = new AtomicReferenceArray<>(count);
        List<CompletableFuture<String>> rqs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int tmpI = i;
            String docId = UUID.randomUUID().toString();
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
                ).thenApply(noth -> indexPath)
                .thenApply(IndexJson::new)
                .thenAccept(index -> index.cacheDocs(docsDto(docId)))
                .thenApply(docInfo -> docsIds.getAndSet(tmpI, docId))
            );
        }
        CompletableFuture.allOf(rqs.toArray(new CompletableFuture[0])).join();
        JsonObject index = new PathConverter(indexPath).toJsonObj();
        for (int i = 0; i < count; i++) {
            MatcherAssert.assertThat(
                index.get(docsIds.get(i)),
                new IsNot<>(new IsNull<>())
            );
        }
    }

    @Test
    void cacheDocsWhenIndexFileNotExist() throws IOException {
        Path indexPath = Paths.get(
            String.format("%s.json", UUID.randomUUID())
        );
        new IndexJson(indexPath).cacheDocs(docsDto(DOC_1_ID));
        MatcherAssert.assertThat(
            new PathConverter(indexPath).toJsonObj().get(DOC_1_ID),
            new IsNot<>(new IsNull<>())
        );
    }

    private List<DocDto> docsDto(String docId) {
        return Collections.singletonList(DocDtoSample.withId(docId));
    }
}
