package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.test.DocDtoSample;
import aakrasnov.diploma.client.utils.PathConverter;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class IndexJsonCacheDocsTest {
    static final String DOC_1_ID = "123abcde123abcde123abcde";

    @TempDir
    private Path tmpDir;

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(tmpDir.toFile());
    }

    @Test
    void cacheDocsWhenIndexFileExist() throws IOException {
        Path indexPath = Files.createTempFile(tmpDir, "cacheDocs", "index.json");
        Files.write(indexPath, new TestResource("index.json").asBytes());
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
        MatcherAssert.assertThat(
            new PathConverter(
                Paths.get(tmpDir.toString(), String.format("%s.json", DOC_1_ID))
            ).toDocDto().getId(),
            new IsEqual<>(DOC_1_ID)
        );
    }

    @Test
    void cacheDocsSimultaneously() throws IOException {
        int count = 100;
        CountDownLatch latch = new CountDownLatch(
            Math.min(count, Runtime.getRuntime().availableProcessors() - 1)
        );
        Path indexPath = Files.createTempFile(tmpDir, "cacheDocsSimultaneously", "index.json");
        Files.write(indexPath, new TestResource("index.json").asBytes());
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
            MatcherAssert.assertThat(
                new PathConverter(
                    Paths.get(tmpDir.toString(), String.format("%s.json", docsIds.get(i)))
                ).toDocDto().getId(),
                new IsEqual<>(docsIds.get(i))
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
