package aakrasnov.diploma.client.api;

import aakrasnov.diploma.common.stata.AddStataRsDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import aakrasnov.diploma.common.stata.UsageDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ClientStatisticApiImplTest {
    public static final String LOCALHOST = "http://localhost:8080/api";

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
        String ptrnId = "pattern_id_1";
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

}