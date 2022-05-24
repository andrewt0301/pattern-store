package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.dto.AddUserRsDto;
import aakrasnov.diploma.client.http.AddSlash;
import aakrasnov.diploma.client.http.ExceptionCatcher;
import aakrasnov.diploma.client.http.RqExecution;
import aakrasnov.diploma.common.UserDto;
import com.google.gson.Gson;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

@Slf4j
public class ClientUserApiImpl implements ClientUserApi {
    private final HttpClient httpClient;

    private final Gson gson;

    /**
     * Base path for URL which should contain schema and host.
     * Port in general is optional.
     */
    private final String base;

    public ClientUserApiImpl(final HttpClient httpClient, final String base) {
        this.httpClient = httpClient;
        this.base = new AddSlash(base).addIfAbsent();
        this.gson = new Gson();
    }

    @Override
    public AddUserRsDto add(final UserDto user) {
        HttpPost rq = new HttpPost(full("user/add"));
        addJsonHeaderTo(rq);
        AddUserRsDto res = new AddUserRsDto();
        new ExceptionCatcher.IOCatcher<>(() -> {
            StringEntity entity = new StringEntity(gson.toJson(user));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to add a user '%s'", user),
                HttpStatus.SC_CREATED
            );
            if (rsp.isPresent()) {
                String body = EntityUtils.toString(rsp.get().getEntity());
                log.info(body);
                res.setUserDto(gson.fromJson(body, UserDto.class));
            }
        }).runAndSetFail(res);
        return res;
    }

    static void addJsonHeaderTo(HttpPost rq) {
        rq.addHeader("Content-type", "application/json");
    }

    private String full(String uri) {
        return String.format("%s%s", base, uri);
    }
}
