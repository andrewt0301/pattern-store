package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.dto.AddUserRsDto;
import aakrasnov.diploma.common.Role;
import aakrasnov.diploma.common.UserDto;
import java.io.IOException;
import java.util.UUID;
import org.apache.http.HttpStatus;
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
class ClientUserApiImplTest {
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
    public void addNewUser() {
        String username = UUID.randomUUID().toString();
        UserDto toAdd = UserDto.builder()
            .username(username)
            .password("user")
            .role(Role.USER)
            .build();
        AddUserRsDto userRs = new ClientUserApiImpl(httpClient, LOCALHOST).add(toAdd);
        MatcherAssert.assertThat(
            "Add user should return CREATED",
            userRs.getStatus(),
            new IsEqual<>(HttpStatus.SC_CREATED)
        );
        MatcherAssert.assertThat(
            "Username of added user should be equal to generated",
            userRs.getUserDto().getUsername(),
            new IsEqual<>(username)
        );
    }

    @Test
    public void failToAddWhenUsernameExist() {
        UserDto toAdd = UserDto.builder()
            .username("user")
            .password("user")
            .role(Role.USER)
            .build();
        AddUserRsDto userRs = new ClientUserApiImpl(httpClient, LOCALHOST).add(toAdd);
        MatcherAssert.assertThat(
            "Add user should return BAD_REQUEST",
            userRs.getStatus(),
            new IsEqual<>(HttpStatus.SC_BAD_REQUEST)
        );
    }

}
