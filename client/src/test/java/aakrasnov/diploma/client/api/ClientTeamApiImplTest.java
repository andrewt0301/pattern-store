package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.team.TeamInfoRsDto;
import java.io.IOException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * This test should run after starting server.
 */
class ClientTeamApiImplTest {
    public static final String LOCALHOST = "http://localhost:8080/api";

    static final String USERNAME_ADMIN = "admin";

    static final String PSWD_ADMIN = "admin";

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
    public void getTeamInfoById() {
        TeamInfoRsDto teamInfo = new ClientTeamApiImpl(httpClient, LOCALHOST)
            .getTeamInfoById("bbbbbbbbbbbbbbbbbbbbbbbb", new User(USERNAME_ADMIN, PSWD_ADMIN));
        MatcherAssert.assertThat(
            "Status should be OK",
            teamInfo.getStatus(),
            new IsEqual<>(200)
        );
        MatcherAssert.assertThat(
            "Invitation code should match",
            teamInfo.getTeamDto().getInvitation(),
            new IsEqual<>("invit-uuid-name3")
        );
    }

    @Test
    public void getTeamInfoByInvite() {
        TeamInfoRsDto teamInfo = new ClientTeamApiImpl(httpClient, LOCALHOST)
             .getTeamInfoByInvite("invit-uuid-name3", new User(USERNAME_ADMIN, PSWD_ADMIN));
        MatcherAssert.assertThat(
            "Status should be OK",
            teamInfo.getStatus(),
            new IsEqual<>(200)
        );
        MatcherAssert.assertThat(
            "Id should match",
            teamInfo.getTeamDto().getId(),
            new IsEqual<>("bbbbbbbbbbbbbbbbbbbbbbbb")
        );
    }

}
