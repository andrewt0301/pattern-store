package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.GetDocRsDto;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * This test should run after starting server.
 */
public class BasicClientDocApiBadCredsTest {
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
    public void emptyForNotExistedUser() {
        GetDocRsDto rs = new BasicClientDocApi(httpClient, BasicClientDocApiTest.LOCALHOST)
            .getDoc(
                BasicClientDocApiTest.DOC_COMMON,
                new User("not exist", "wrong pswd")
            );
        MatcherAssert.assertThat(
            "Return null for unfamiliar user",
            rs.getDocDto(),
            new IsNull<>()
        );
        MatcherAssert.assertThat(
            "Status should be UNAUTHORIZED",
            rs.getStatus(),
            new IsEqual<>(HttpStatus.SC_UNAUTHORIZED)
        );
    }
}
