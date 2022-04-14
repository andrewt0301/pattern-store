package aakrasnov.diploma.client;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.RsBaseDto;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.util.EntityUtils;

@Slf4j
final class BasicClientApi implements ClientApi {

    private final HttpClient httpClient;

    private Gson gson;

    /**
     * Base path for URL which should contain schema and host.
     * Port in general is optional.
     */
    private final String base;

    BasicClientApi(final HttpClient httpClient, final String base) {
        this.httpClient = httpClient;
        this.base = addSlashIfAbsent(base);
        this.gson = new Gson();
    }

    @Override
    public Optional<DocDto> getDocFromCommon(final String id) {
        HttpGet rq = new HttpGet(full(String.format("doc/%s", id)));
        try {
            HttpResponse rsp = httpClient.execute(rq);
            if (rsp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return Optional.of(
                    gson.fromJson(EntityUtils.toString(rsp.getEntity()), DocDto.class)
                );
            }
            log.error("status: {}", rsp.getStatusLine().toString());
        } catch (IOException exc) {
            log.error(
                String.format("Failed to get document from common pool by id '%s'", id),
                exc
            );
        }
        return Optional.empty();
    }

    @Override
    public List<DocDto> filterDocsFromCommon(final List<Filter> filters) {
        HttpPost rq = new HttpPost(full("docs/filtered"));
        addJsonHeaderTo(rq);
        try {
            StringEntity entity = new StringEntity(gson.toJson(filters));
            rq.setEntity(entity);
            HttpResponse rsp = httpClient.execute(rq);
            if (rsp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return Arrays.asList(
                    gson.fromJson(EntityUtils.toString(rsp.getEntity()), DocDto[].class)
                );
            }
            log.error("status: {}", rsp.getStatusLine().toString());
        } catch (IOException exc) {
            log.error(
                String.format("Failed to filter documents from common pool by id '%s'", filters),
                exc
            );
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<DocDto> getDoc(final String id, final User user) {
        HttpGet rq = new HttpGet(full(String.format("auth/doc/%s", id)));
        try {
            addBasicAuthorization(rq, user);
            HttpResponse rsp = httpClient.execute(rq);
            if (rsp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return Optional.of(
                    gson.fromJson(EntityUtils.toString(rsp.getEntity()), DocDto.class)
                );
            }
            log.error("status: {}", rsp.getStatusLine().toString());
        } catch (IOException exc) {
            log.error(
                String.format("Failed to get document by id '%s'", id),
                exc
            );
        } catch (AuthenticationException exc) {
            log.error(
                String.format("Unable to generate basic scheme header for user '%s'", user),
                exc
            );
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(final String id, final User user) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void add(final DocDto document, final User user) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public RsBaseDto update(final String id, final DocDto docUpd, final User user) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public List<DocDto> filterDocuments(final List<Filter> filters, final User user) {
        throw new RuntimeException("not implemented yet");
    }

    private static String addSlashIfAbsent(String base) {
        if (!base.endsWith("/")) {
            return String.format("%s/", base);
        }
        return base;
    }

    private static void addJsonHeaderTo(HttpPost rq) {
        rq.addHeader("Content-type", "application/json");
    }

    private static void addBasicAuthorization(HttpRequestBase rq, User user)
        throws AuthenticationException
    {
        UsernamePasswordCredentials creds;
        creds = new UsernamePasswordCredentials(user.getUsername(), user.getPassword());
        rq.addHeader(new BasicScheme().authenticate(creds, rq, null));
    }

    private String full(String uri) {
        return String.format("%s%s", base, uri);
    }
}
