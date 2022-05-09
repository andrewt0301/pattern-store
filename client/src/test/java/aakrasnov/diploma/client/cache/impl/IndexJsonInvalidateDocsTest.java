package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.test.PathConverter;
import aakrasnov.diploma.client.test.TestResource;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;

public class IndexJsonInvalidateDocsTest {

    @Test
    void invalidateExistingDocInIndexFile() throws IOException {
        String tmpIndex = "tmp-index.json";
        Path tmpDir = Files.createTempDirectory("invalidate");
        Path tmpIndexPath = Files.createFile(Paths.get(tmpDir.toString(), tmpIndex));
        Files.write(tmpIndexPath, new TestResource("index.json").asBytes());
        Files.write(
            Paths.get(
                tmpDir.toString(),
                new PathConverter(tmpIndexPath).toJsonObj()
                    .get(IndexFileJsonTest.CACHE_DOC)
                    .getAsJsonObject()
                    .get(IndexFileJson.DOC_PATH)
                    .getAsString()
            ),
            new byte[] {}
        );
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
        FileUtils.deleteDirectory(tmpDir.toFile());
    }

    @Test
    void invalidateNotExistingDocInIndexFile() throws IOException {
        String tmpIndex = "tmp-index.json";
        String absentDocId = "absent document id";
        Path tmpIndexPath = Files.createTempFile("path", tmpIndex);
        Files.write(tmpIndexPath, new TestResource("index.json").asBytes());
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
        Files.delete(tmpIndexPath);
    }
}
