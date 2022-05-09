package aakrasnov.diploma.client.api.cache;

import aakrasnov.diploma.client.api.ClientDocApi;
import aakrasnov.diploma.client.cache.CachedDocInfo;
import aakrasnov.diploma.client.cache.impl.IndexFileJson;
import aakrasnov.diploma.client.cache.impl.IndexJson;
import aakrasnov.diploma.client.dto.GetDocRsDto;
import aakrasnov.diploma.client.test.DocDtoSample;
import aakrasnov.diploma.client.utils.TimeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

class CacheIndexClientDocTest {
    @Test
    void getFromCommonWhenDocumentIsCached() throws IOException {
        String tmpIndex = "my-index.json";
        String prefix = "prefix";
        Path tmpDir = Files.createTempDirectory("some-temp-dir");
        Path tmpIndexPath = Files.createFile(Paths.get(tmpDir.toString(), tmpIndex));
        String docId = "159357159357159357159357";
        String docTimestamp = new TimeConverter(LocalDateTime.now()).asString();
        Path docDir = Files.createDirectory(tmpDir.resolve(prefix));
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(
            IndexFileJson.CACHE_CREATION,
            new TimeConverter(LocalDateTime.now()).asString()
        );
        Path cachedDocPath = Files.createFile(
            Paths.get(docDir.toString(), String.format("%s-doc.json", docId))
        );
        Files.write(
            cachedDocPath,
            new Gson().toJson(DocDtoSample.withIdAndTimestamp(docId, docTimestamp)).getBytes()
        );
        jsonObj.add(
            docId,
            CachedDocInfo.builder()
                .path(Paths.get(prefix, cachedDocPath.getFileName().toString()).toString())
                .cachingTime(new TimeConverter(LocalDateTime.now()).asString())
                .docTimestamp(docTimestamp)
                .build()
                .toJsonObject()
        );
        Files.write(tmpIndexPath, jsonObj.toString().getBytes());
        try {
            GetDocRsDto res = new CacheIndexClientDoc(
                new ClientDocApi.Fake(),
                new IndexJson(tmpIndexPath)
            ).getDocFromCommon(docId);
            MatcherAssert.assertThat(
                "Status should be OK",
                res.getStatus(),
                new IsEqual<>(HttpStatus.SC_OK)
            );
            MatcherAssert.assertThat(
                res.getDocDto().getId(),
                new IsEqual<>(docId)
            );
        } finally {
            FileUtils.deleteDirectory(docDir.toFile());
            FileUtils.deleteDirectory(tmpDir.toFile());
        }
    }

}
