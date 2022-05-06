package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.AddDocRsDto;
import aakrasnov.diploma.client.dto.DocsRsDto;
import aakrasnov.diploma.client.dto.GetDocRsDto;
import aakrasnov.diploma.client.dto.UpdateDocRsDto;
import aakrasnov.diploma.client.utils.TimeConverter;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.common.ScenarioDto;
import aakrasnov.diploma.common.TeamDto;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
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
class BasicClientDocApiTest {
    public static final String LOCALHOST = "http://localhost:8080/api";

    public static final String ADMIN_USERNAME = "admin";

    public static final String ADMIN_PSWD = "admin";

    public static final String USER_USERNAME = "user";

    public static final String USER_PSWD = "user";

    public static final String ONLYTEST_USERNAME = "onlytest";

    public static final String ONLYTEST_PSWD = "onlytest";

    public static final String DOC_COMMON = "625748988af05121cc0d6189";

    public static final String TEAM_PRIVATE = "aaccbbffdd11223344556677";

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
        GetDocRsDto doc = new BasicClientDocApi(httpClient, LOCALHOST)
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
        DocsRsDto docs = new BasicClientDocApi(httpClient, LOCALHOST)
            .filterDocsFromCommon(Collections.emptyList());
        MatcherAssert.assertThat(
            "Documents should not be null",
            docs.getDocs(),
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            "Should be exactly 1 document from common pool",
            docs.getDocs().size(),
            new IsEqual<>(1)
        );
    }

    @Test
    public void getDocByIdForAdmin() {
        MatcherAssert.assertThat(
            "Saved document should be obtained for admin",
            new BasicClientDocApi(httpClient, LOCALHOST)
                .getDoc(
                    DOC_COMMON,
                    new User(ADMIN_USERNAME, ADMIN_PSWD)
                ).getDocDto(),
            Matchers.notNullValue()
        );
    }

    @Test
    public void getAllDocsAvailableForUser() {
        DocsRsDto res = new BasicClientDocApi(httpClient, LOCALHOST)
            .getAllDocsForUser(new User(ONLYTEST_USERNAME, ONLYTEST_PSWD));
        System.out.println(res);
        MatcherAssert.assertThat(
            "Status should be OK",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Should obtain 2 docs for test user",
            res.getDocs().size(),
            new IsEqual<>(2)
        );
        MatcherAssert.assertThat(
            "Should contain 'java' and 'js' languages",
            res.getDocs().stream()
                .map(DocDto::getLang)
                .collect(Collectors.toList()),
            Matchers.containsInAnyOrder("java", "js")
        );
    }

    @Test
    public void getDocsByTeamIdWhenUserInTeam() {
        DocsRsDto res = new BasicClientDocApi(httpClient, LOCALHOST)
            .getDocsByTeamId(TEAM_PRIVATE, new User(USER_USERNAME, USER_PSWD));
        MatcherAssert.assertThat(
            "Status should be OK",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_OK)
        );
        MatcherAssert.assertThat(
            "Should be exactly 1 doc",
            res.getDocs().size(),
            new IsEqual<>(1)
        );
        MatcherAssert.assertThat(
            "Scenario should be FOR_TEST",
            res.getDocs().get(0).getScenario().getType(),
            new IsEqual<>(ScenarioDto.Type.FOR_TEST)
        );
    }

    @Test
    public void forbiddenGetDocsByTeamIdWhenUserNotInTeam() {
        DocsRsDto res = new BasicClientDocApi(httpClient, LOCALHOST)
            .getDocsByTeamId(TEAM_PRIVATE, new User(ONLYTEST_USERNAME, ONLYTEST_PSWD));
        MatcherAssert.assertThat(
            "Status should be FORBIDDEN",
            res.getStatus(),
            new IsEqual<>(HttpStatus.SC_FORBIDDEN)
        );
    }

    @Test
    public void getDocByIdWhenUserFromTeam() {
        MatcherAssert.assertThat(
            "Saved document should be obtained for user from team",
            new BasicClientDocApi(httpClient, LOCALHOST)
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
            new BasicClientDocApi(httpClient, LOCALHOST)
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
            new BasicClientDocApi(httpClient, LOCALHOST)
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
        AddDocRsDto docRs = new BasicClientDocApi(
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
        DocsRsDto docs = new BasicClientDocApi(httpClient, LOCALHOST)
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
        BasicClientDocApi clientApi = new BasicClientDocApi(httpClient, LOCALHOST);
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
        docNew.setTimestamp(new TimeConverter(LocalDateTime.now()).asString());
        return docNew;
    }

}
