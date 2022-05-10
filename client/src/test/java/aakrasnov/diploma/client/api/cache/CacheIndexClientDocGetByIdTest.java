package aakrasnov.diploma.client.api.cache;

import aakrasnov.diploma.client.api.ClientDocApi;
import aakrasnov.diploma.client.cache.impl.IndexFileJson;
import aakrasnov.diploma.client.cache.impl.IndexFileJsonTest;
import aakrasnov.diploma.client.cache.impl.IndexJson;
import aakrasnov.diploma.client.dto.GetDocRsDto;
import aakrasnov.diploma.client.test.DocDtoSample;
import aakrasnov.diploma.client.test.TestResource;
import aakrasnov.diploma.client.utils.PathConverter;
import aakrasnov.diploma.common.cache.DocValidityCheckRsDto;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

public class CacheIndexClientDocGetByIdTest {
    private Path tmpDir;

    @BeforeEach
    void setUp() throws IOException {
        tmpDir = Files.createTempDirectory("cache-index-doc-get");
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(tmpDir.toFile());
    }

    @ParameterizedTest
    @MethodSource("answersWhenGetFromCache")
    void getDocFromCacheWhenServerAnswerExistByIdAndTimestampOrNotExist(
        DocValidityCheckRsDto.ServerAnswer serverAnswer
    ) throws IOException {
        DocValidityCheckRsDto validityRs = new DocValidityCheckRsDto();
        validityRs.setServerAnswer(serverAnswer);
        ClientDocApi docApi = Mockito.spy(ClientDocApi.class);
        Mockito.when(
            docApi.checkDocValidityByTimestampFromCommon(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(validityRs);
        String docTimestamp = "2022:03:22 11:01:10";
        Path indexPath = getIndexPathAndCacheDoc(docTimestamp);
        GetDocRsDto docRs = new CacheIndexClientDoc(docApi, new IndexJson(indexPath))
            .getDocFromCommon(IndexFileJsonTest.CACHE_DOC);
        MatcherAssert.assertThat(
            "Status should be OK",
            docRs.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            docRs.getDocDto().getId(),
            new IsEqual<>(IndexFileJsonTest.CACHE_DOC)
        );
        MatcherAssert.assertThat(
            docRs.getDocDto().getTimestamp(),
            new IsEqual<>(docTimestamp)
        );
    }

    @Test
    void getDocFromServerWhenServerAnswerExistOnlyById() throws IOException {
        DocValidityCheckRsDto validityRs = new DocValidityCheckRsDto();
        String updTimestamp = "2022:04:18 08:54:58";
        // Doc with this id should be cached in order to invalidate.
        String docId = IndexFileJsonTest.CACHE_DOC;
        validityRs.setDocDto(DocDtoSample.withIdAndTimestamp(docId, updTimestamp));
        validityRs.setServerAnswer(DocValidityCheckRsDto.ServerAnswer.EXIST_ONLY_BY_ID);
        ClientDocApi docApi = Mockito.spy(ClientDocApi.class);
        Mockito.when(
            docApi.checkDocValidityByTimestampFromCommon(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(validityRs);
        Path indexPath = getIndexPathAndCacheDoc("2022:03:22 11:01:10");
        GetDocRsDto docRs = new CacheIndexClientDoc(
            docApi, new IndexJson(indexPath)
        ).getDocFromCommon(docId);
        MatcherAssert.assertThat(
            "Status should be OK",
            docRs.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Should return doc with new timestamp",
            docRs.getDocDto().getTimestamp(),
            new IsEqual<>(updTimestamp)
        );
        MatcherAssert.assertThat(
            "Old value in cache should be invalidated, and the new one is cached",
            new PathConverter(indexPath).toJsonObj()
                .get(docId).getAsJsonObject()
                .get(IndexFileJson.DOC_TIMESTAMP).getAsString(),
            new IsEqual<>(updTimestamp)
        );
    }

    private Path getIndexPathAndCacheDoc(String docTimestamp) throws IOException {
        Path indexPath = Files.write(
            Paths.get(tmpDir.toString(), "index.json"), new TestResource("index.json").asBytes()
        );
        Files.write(
            Paths.get(tmpDir.toString(), "pathToCachedFileAndDocId.json"),
            new Gson().toJson(
                DocDtoSample.withIdAndTimestamp(IndexFileJsonTest.CACHE_DOC, docTimestamp)
            ).getBytes()
        );
        return indexPath;
    }

    private static Stream<Arguments> answersWhenGetFromCache() {
        return Stream.of(
            Arguments.of(DocValidityCheckRsDto.ServerAnswer.EXIST_BY_ID_AND_TIMESTAMP),
            Arguments.of(DocValidityCheckRsDto.ServerAnswer.NOT_EXIST)
        );
    }

}
