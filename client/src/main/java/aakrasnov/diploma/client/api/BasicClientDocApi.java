package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.AddDocRsDto;
import aakrasnov.diploma.client.dto.DocsRsDto;
import aakrasnov.diploma.client.dto.GetDocRsDto;
import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.client.dto.UpdateDocRsDto;
import aakrasnov.diploma.client.http.AddSlash;
import aakrasnov.diploma.client.http.RqExecution;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.util.EntityUtils;

@Slf4j
public final class BasicClientDocApi implements ClientDocApi {

    private final HttpClient httpClient;

    private final Gson gson;

    /**
     * Base path for URL which should contain schema and host.
     * Port in general is optional.
     */
    private final String base;

    public BasicClientDocApi(final HttpClient httpClient, final String base) {
        this.httpClient = httpClient;
        this.base = new AddSlash(base).addIfAbsent();
        this.gson = new Gson();
    }

    @Override
    public GetDocRsDto getDocFromCommon(final String id) {
        HttpGet rq = new HttpGet(full(String.format("doc/%s", id)));
        GetDocRsDto res = new GetDocRsDto();
        try {
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to get document from common pool by id '%s'", id)
            );
            if (rsp.isPresent()) {
                res.setDocDto(
                    gson.fromJson(EntityUtils.toString(rsp.get().getEntity()), DocDto.class)
                );
                return res;
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    @Override
    public DocsRsDto filterDocsFromCommon(final List<Filter> filters) {
        HttpPost rq = new HttpPost(full("docs/filtered"));
        addJsonHeaderTo(rq);
        DocsRsDto res = new DocsRsDto();
        try {
            StringEntity entity = new StringEntity(gson.toJson(filters));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format(
                    "Failed to filter documents from common pool by filters '%s'", filters
                )
            );
            if (rsp.isPresent()) {
                res.setDocs(
                    Arrays.asList(
                        gson.fromJson(EntityUtils.toString(rsp.get().getEntity()), DocDto[].class)
                    )
                );
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    @Override
    public GetDocRsDto getDoc(final String id, final User user) {
        HttpGet rq = new HttpGet(full(String.format("auth/doc/%s", id)));
        GetDocRsDto res = new GetDocRsDto();
        try {
            addBasicAuthorization(rq, user, res);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to get document by id '%s'", id)
            );
            if (rsp.isPresent()) {
                res.setDocDto(
                    gson.fromJson(EntityUtils.toString(rsp.get().getEntity()), DocDto.class)
                );
                return res;
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    @Override
    public RsBaseDto deleteById(final String id, final User user) {
        HttpDelete rq = new HttpDelete(full(String.format("admin/doc/%s/delete", id)));
        RsBaseDto res = new RsBaseDto();
        addBasicAuthorization(rq, user, res);
        new RqExecution(httpClient, rq).execAnSetStatus(
            res,
            String.format("Failed to delete document by id '%s'", id)
        );
        return res;
    }

    @Override
    public AddDocRsDto add(final DocDto document, final User user) {
        HttpPost rq = new HttpPost(full("auth/doc"));
        addJsonHeaderTo(rq);
        AddDocRsDto res = new AddDocRsDto();
        try {
            addBasicAuthorization(rq, user, res);
            StringEntity entity = new StringEntity(gson.toJson(document));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to add document '%s'", document),
                HttpStatus.SC_CREATED
            );
            if (rsp.isPresent()) {
                res.setDocDto(
                    gson.fromJson(EntityUtils.toString(rsp.get().getEntity()), DocDto.class)
                );
                return res;
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    @Override
    public UpdateDocRsDto update(final String id, final DocDto docUpd, final User user) {
        HttpPost rq = new HttpPost(full(String.format("auth/doc/%s/update", id)));
        addJsonHeaderTo(rq);
        UpdateDocRsDto res = new UpdateDocRsDto();
        try {
            addBasicAuthorization(rq, user, res);
            StringEntity entity = new StringEntity(gson.toJson(docUpd));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to update doc with id '%s' with '%s'", id, docUpd.toString())
            );
            if (rsp.isPresent()) {
                res.setDocDto(
                    gson.fromJson(EntityUtils.toString(rsp.get().getEntity()), DocDto.class)
                );
                return res;
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    @Override
    public DocsRsDto filterDocuments(final List<Filter> filters, final User user) {
        HttpPost rq = new HttpPost(full("auth/docs/filtered"));
        addJsonHeaderTo(rq);
        DocsRsDto res = new DocsRsDto();
        try {
            addBasicAuthorization(rq, user, res);
            StringEntity entity = new StringEntity(gson.toJson(filters));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to filter documents by id '%s'", filters)
            );
            if (rsp.isPresent()) {
                res.setDocs(
                    Arrays.asList(
                        gson.fromJson(EntityUtils.toString(rsp.get().getEntity()), DocDto[].class)
                    )
                );
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    static void addJsonHeaderTo(HttpPost rq) {
        rq.addHeader("Content-type", "application/json");
    }

    private static void addBasicAuthorization(HttpRequestBase rq, User user, RsBaseDto res) {
        UsernamePasswordCredentials creds;
        creds = new UsernamePasswordCredentials(user.getUsername(), user.getPassword());
        try {
            rq.addHeader(new BasicScheme().authenticate(creds, rq, null));
        } catch (AuthenticationException exc) {
            String err = String.format(
                "Unable to generate basic scheme header for user '%s'", user
            );
            log.error(err, exc);
            res.setStatus(HttpStatus.SC_UNAUTHORIZED);
            res.setMsg(err);
        }
    }

    private String full(String uri) {
        return String.format("%s%s", base, uri);
    }

}
