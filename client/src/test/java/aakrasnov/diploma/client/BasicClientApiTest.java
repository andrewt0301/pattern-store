package aakrasnov.diploma.client;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.common.DocDto;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
class BasicClientApiTest {
    public static final String LOCALHOST = "http://localhost:8080";

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
    public void getFromCommonById() {
        Optional<DocDto> doc = new BasicClientApi(httpClient, LOCALHOST)
            .getDocFromCommon("625748988af05121cc0d6189");
        MatcherAssert.assertThat(
            "Document from mongock scripts must exist",
            doc.isPresent(),
            new IsEqual<>(true)
        );
        MatcherAssert.assertThat(
            "Lang for dummy document is wrong",
            doc.get().getLang(),
            new IsEqual<>("java")
        );
    }

    @Test
    public void getAllDocsFromCommon() {
        List<DocDto> docs = new BasicClientApi(httpClient, LOCALHOST)
            .filterDocsFromCommon(Collections.emptyList());
        MatcherAssert.assertThat(
            "Should be exactly 2 documents from common pool",
            docs.size(),
            new IsEqual<>(2)
        );
    }

    @Test
    public void getDocForAdmin() {
        MatcherAssert.assertThat(
            "Saved document should be obtained for admin",
            new BasicClientApi(httpClient, LOCALHOST)
                .getDoc(
                    "625748988af05121cc0d6189",
                    new User("admin", "admin")
                ).isPresent(),
            new IsEqual<>(true)
        );
    }

    @Test
    public void getDocWhenUserFromTeam() {
        MatcherAssert.assertThat(
            "Saved document should be obtained for user from team",
            new BasicClientApi(httpClient, LOCALHOST)
                .getDoc(
                    "625748988af05121cc0d6189",
                    new User("user", "user")
                ).isPresent(),
            new IsEqual<>(true)
        );
    }

    @Test
    public void emptyForNotExitedUser() {
        MatcherAssert.assertThat(
            "Return empty for unfamiliar user",
            new BasicClientApi(httpClient, LOCALHOST)
                .getDoc(
                    "625748988af05121cc0d6189",
                    new User("not exist", "wrong pswd")
                ).isPresent(),
            new IsEqual<>(false)
        );
    }

}
