package aakrasnov.diploma.client;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.AddDocRsDto;
import aakrasnov.diploma.client.dto.DocsRsDto;
import aakrasnov.diploma.client.dto.GetDocRsDto;
import aakrasnov.diploma.client.dto.UpdateDocRsDto;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.common.ScenarioDto;
import aakrasnov.diploma.common.TeamDto;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * This test should run after starting server.
 */
class BasicClientApiTest {
    public static final String LOCALHOST = "http://localhost:8080";

    public static final String ADMIN_USERNAME = "admin";

    public static final String ADMIN_PSWD = "admin";

    public static final String USER_USERNAME = "user";

    public static final String USER_PSWD = "user";

    public static final String DOC_COMMON = "625748988af05121cc0d6189";

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
        GetDocRsDto doc = new BasicClientApi(httpClient, LOCALHOST)
            .getDocFromCommon(DOC_COMMON);
        MatcherAssert.assertThat(
            "Document from mongock scripts must exist",
            doc.getDocDto(),
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Scenario for dummy document is wrong",
            doc.getDocDto().getScenario().getType(),
            new IsEqual<>(ScenarioDto.Type.MIGRATION)
        );
    }

    @Test
    public void getAllDocsFromCommon() {
        DocsRsDto docs = new BasicClientApi(httpClient, LOCALHOST)
            .filterDocsFromCommon(Collections.emptyList());
        MatcherAssert.assertThat(
            "Documents should not be null",
            docs.getDocs(),
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Should be exactly 2 documents from common pool",
            docs.getDocs().size(),
            new IsEqual<>(2)
        );
    }

    @Test
    public void getDocForAdmin() {
        MatcherAssert.assertThat(
            "Saved document should be obtained for admin",
            new BasicClientApi(httpClient, LOCALHOST)
                .getDoc(
                    DOC_COMMON,
                    new User(ADMIN_USERNAME, ADMIN_PSWD)
                ).getDocDto(),
            Matchers.notNullValue()
        );
    }

    @Test
    public void getDocWhenUserFromTeam() {
        MatcherAssert.assertThat(
            "Saved document should be obtained for user from team",
            new BasicClientApi(httpClient, LOCALHOST)
                .getDoc(
                    DOC_COMMON,
                    new User(USER_USERNAME, USER_PSWD)
                ).getDocDto(),
            Matchers.notNullValue()
        );
    }

    @Test
    @Disabled("This test affect other tests, it must rollback after completion")
    public void deleteDocByIdForAdmin() {
        MatcherAssert.assertThat(
            "Should return OK for successful delete or if document is absent",
            new BasicClientApi(httpClient, LOCALHOST)
                .deleteById(
                    DOC_COMMON,
                    new User(ADMIN_USERNAME, ADMIN_PSWD)
                ).getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
    }

    @Test
    public void returnForbiddenForDeleteByUserWhoNotAdmin() {
        MatcherAssert.assertThat(
            "Should be forbidden for not admin",
            new BasicClientApi(httpClient, LOCALHOST)
                .deleteById(
                    DOC_COMMON,
                    new User(USER_USERNAME, USER_PSWD)
                ).getStatus(),
            new IsEqual<>(HttpStatus.SC_FORBIDDEN)
        );
    }

    @Test
    public void addDocumentByUser() {
        DocDto toAdd = getDoc();
        AddDocRsDto docRs = new BasicClientApi(
            httpClient, LOCALHOST
        ).add(toAdd, new User(USER_USERNAME, USER_PSWD));
        MatcherAssert.assertThat(
            "Add doc should return CREATED",
            docRs.getStatus(),
            new IsEqual<>(HttpStatus.SC_CREATED)
        );
        MatcherAssert.assertThat(
            "Timestamps of created and saved docs should be equal",
            docRs.getDocDto().getTimestamp(),
            new IsEqual<>(toAdd.getTimestamp())
        );
    }

    @Test
    public void filterDocsForUser() {
        System.out.println(ScenarioDto.Type.FOR_TEST.name());
        DocsRsDto docs = new BasicClientApi(httpClient, LOCALHOST)
            .filterDocuments(
                Collections.singletonList(
                    new Filter.Wrap("scenario.type", ScenarioDto.Type.FOR_TEST.name())
                ),
                new User(USER_USERNAME, USER_PSWD)
            );
        MatcherAssert.assertThat(
            "Documents should not be null",
            docs.getDocs(),
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Should be exactly 1 document with 'FOR_TEST' scenario for user 'user'",
            docs.getDocs().size(),
            new IsEqual<>(1)
        );
    }

    @Test
    public void updateDoc() {
        String lang = "new lang java";
        BasicClientApi clientApi = new BasicClientApi(httpClient, LOCALHOST);
        GetDocRsDto docRs = clientApi.getDocFromCommon(DOC_COMMON);
        docRs.getDocDto().setLang(lang);
        UpdateDocRsDto updDoc = clientApi.update(
            DOC_COMMON, docRs.getDocDto(), new User(USER_USERNAME, USER_PSWD)
        );
        MatcherAssert.assertThat(
            "Status should be OK",
            updDoc.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Language should be updated",
            clientApi.getDocFromCommon(DOC_COMMON).getDocDto().getLang(),
            new IsEqual<>(lang)
        );
    }

    public DocDto getDoc() {
        DocDto docNew = new DocDto();
        TeamDto team = new TeamDto();
        ScenarioDto scenario = new ScenarioDto();
        scenario.setType(ScenarioDto.Type.UNKNOWN);
        team.setId("b5bd5adf7b5bd93e56f6d5d8");
        team.setName("team2_private");
        team.setCreatorId("361d5d8b5bd5adf7ee8793e5");
        docNew.setTeam(team);
        docNew.setLang("java");
        docNew.setPatterns(new ArrayList<>());
        docNew.setScenario(scenario);
        docNew.setTimestamp(LocalDateTime.now().format(DocDto.DATE_FORMATTER).toString());
        return docNew;
    }

}
