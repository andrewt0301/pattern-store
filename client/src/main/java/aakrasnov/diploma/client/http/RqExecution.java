package aakrasnov.diploma.client.http;

import aakrasnov.diploma.common.RsBaseDto;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

@Slf4j
public final class RqExecution {
    private final HttpClient httpClient;

    private final HttpRequestBase request;

    public RqExecution(final HttpClient httpClient, final HttpRequestBase request) {
        this.httpClient = httpClient;
        this.request = request;
    }

    public Optional<HttpResponse> execAnSetStatus(RsBaseDto res, String errMsg) {
        return execAnSetStatus(res, errMsg, HttpStatus.SC_OK);
    }

    public Optional<HttpResponse> execAnSetStatus(
        RsBaseDto res, String errMsg, int successStatus
    ) {
        try {
            HttpResponse rsp = httpClient.execute(request);
            if (rsp.getStatusLine().getStatusCode() == successStatus) {
                res.setStatus(successStatus);
                return Optional.of(rsp);
            } else {
                log.error("status: {}", rsp.getStatusLine().toString());
                log.error(errMsg);
                res.setStatus(rsp.getStatusLine().getStatusCode());
                res.setMsg(rsp.getStatusLine().getReasonPhrase());
            }
        } catch (IOException exc) {
            log.error(errMsg, exc);
            res.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            res.setMsg(errMsg);
        }
        return Optional.empty();
    }
}
