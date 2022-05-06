package aakrasnov.diploma.client.api;

import aakrasnov.diploma.common.stata.AddStataRsDto;
import aakrasnov.diploma.common.stata.DocIdDto;
import aakrasnov.diploma.common.stata.GetDownloadDocsRsDto;
import aakrasnov.diploma.common.stata.GetStataDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedDocRsDto;
import aakrasnov.diploma.common.stata.GetStataPtrnsRsDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import aakrasnov.diploma.common.stata.UsageDto;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ClientStatisticApiImplTest {
    public static final String LOCALHOST = "http://localhost:8080/api";

    static final String PATTERN_1_ID = "111111111111111111111111";

    static final String PATTERN_2_ID = "222222222222222222222222";

    private static CloseableHttpClient httpClient;

    @BeforeAll
    static void setUp() {
        httpClient = HttpClients.createDefault();
    }

    @AfterAll
    static void tearDown() throws IOException {
        httpClient.close();
    }

    @Test
    void sendStatisticAboutDocs() {
        List<StatisticDto> toSend = new ArrayList<>();
        StatisticDto stata = new StatisticDto();
        String ptrnId = "addd0caddd0caddd0c000001";
        stata.setDocumentId(BasicClientDocApiTest.DOC_COMMON);
        stata.setSuccess(null);
        stata.setFailure(new ArrayList<>());
        stata.setDownload(
            Collections.singletonList(
                new UsageDto(2, ptrnId, new HashMap<>())
            )
        );
        toSend.add(stata);
        AddStataRsDto res = new ClientStatisticApiImpl(httpClient, LOCALHOST)
            .sendDocStatistic(toSend);
        MatcherAssert.assertThat(
            "Status should be CREATED",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_CREATED)
        );
        MatcherAssert.assertThat(
            res.getStatisticDocs().get(0)
                .getDownload().get(0)
                .getPatternId(),
            new IsEqual<>(ptrnId)
        );
    }

    @Test
    void getStatisticByPatternsId() {
        String frst = PATTERN_1_ID;
        String scnd = PATTERN_2_ID;
        Set<String> patternIds = ImmutableSet.of(frst, scnd);
        GetStataPtrnsRsDto res = new ClientStatisticApiImpl(httpClient, LOCALHOST)
            .getStatisticForPatterns(patternIds);
        MatcherAssert.assertThat(
            "Status should be OK",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Should be 4 pattern statistics in db after db initialization",
            res.getPtrnsStatas().size(),
            new IsEqual<>(4)
        );
        MatcherAssert.assertThat(
            "Should contain entries for both patterns",
            res.getPtrnsStatas().get(0).getDownload()
                .stream().map(UsageDto::getPatternId)
                .collect(Collectors.toSet()),
            Matchers.containsInAnyOrder(frst, scnd)
        );
    }

    @Test
    void getMergedStatisticByPatternId() {
        GetStataPtrnsRsDto res = new ClientStatisticApiImpl(httpClient, LOCALHOST)
            .getStatisticForPatterns(ImmutableSet.of(PATTERN_1_ID));
        MatcherAssert.assertThat(
            "Status should be OK",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Should be 4 pattern in db after db initialization",
            res.getPtrnsStatas().size(),
            new IsEqual<>(4)
        );
    }

    @Test
    void getStatisticByDocId() {
        String docId = "11335577992244668800abcd";
        GetStataDocRsDto res = new ClientStatisticApiImpl(httpClient, LOCALHOST)
            .getStatisticForDoc(new DocIdDto(docId));
        MatcherAssert.assertThat(
            "Status should be OK",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Should be 2 doc statistics in db after db initialization",
            res.getDocStatas().size(),
            new IsEqual<>(2)
        );
        MatcherAssert.assertThat(
            "Should contain 4 entries for document",
            res.getDocStatas().get(0)
                .getStataPtrns()
                .getDownload()
                .size(),
            new IsEqual<>(4)
        );
    }

    @Test
    void getStatisticMergedByDocId() {
        String docId = "11335577992244668800abcd";
        GetStataMergedDocRsDto res = new ClientStatisticApiImpl(httpClient, LOCALHOST)
            .getStatisticUsageMergedForDoc(new DocIdDto(docId));
        MatcherAssert.assertThat(
            "Status should be OK",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Should be 4 entries in success in db after db initialization",
            res.getDownload().size(),
            new IsEqual<>(4)
        );
        MatcherAssert.assertThat(
            "Total result should be properly calculated",
            res.getSuccess().get(PATTERN_1_ID),
            new IsEqual<>(19)
        );
    }

    @Test
    void getStatisticDocsDownloadsCount() {
        String frst = "11335577992244668800abcd";
        String scnd = BasicClientDocApiTest.DOC_COMMON;
        GetDownloadDocsRsDto res = new ClientStatisticApiImpl(httpClient, LOCALHOST)
            .getDownloadsCountForDocs(ImmutableSet.of(frst, scnd));
        MatcherAssert.assertThat(
            "Status should be OK",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Downloads doc count should be calculated correctly",
            res.getDocsDownloads().get(frst),
            new IsEqual<>(2)
        );
    }
}
