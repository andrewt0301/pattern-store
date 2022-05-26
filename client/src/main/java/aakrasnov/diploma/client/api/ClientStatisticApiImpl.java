package aakrasnov.diploma.client.api;

import aakrasnov.diploma.client.http.AddSlash;
import aakrasnov.diploma.client.http.ExceptionCatcher;
import aakrasnov.diploma.client.http.RqExecution;
import aakrasnov.diploma.common.stata.AddStataRsDto;
import aakrasnov.diploma.common.stata.DocIdDto;
import aakrasnov.diploma.common.stata.GetDownloadDocsRsDto;
import aakrasnov.diploma.common.stata.GetStataDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedPtrnsRsDto;
import aakrasnov.diploma.common.stata.GetStataPtrnsRsDto;
import aakrasnov.diploma.common.stata.IdsDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

@Slf4j
public class ClientStatisticApiImpl implements ClientStatisticApi {
    private final HttpClient httpClient;

    private final Gson gson;

    /**
     * Base path for URL which should contain schema and host.
     * Port in general is optional.
     */
    private final String base;

    public ClientStatisticApiImpl(final HttpClient httpClient, final String base) {
        this.httpClient = httpClient;
        this.base = new AddSlash(base).addIfAbsent();
        this.gson = new Gson();
    }

    @Override
    public AddStataRsDto sendDocStatistic(final List<StatisticDto> statistics) {
        HttpPost rq = new HttpPost(full("statistic"));
        BasicClientDocApi.addJsonHeaderTo(rq);
        AddStataRsDto res = new AddStataRsDto();
        new ExceptionCatcher.IOCatcher<>(() -> {
            StringEntity entity = new StringEntity(gson.toJson(statistics));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to upload statistic '%s'", statistics),
                HttpStatus.SC_CREATED
            );
            if (rsp.isPresent()) {
                String body = EntityUtils.toString(rsp.get().getEntity());
                log.info(body);
                res.setStatisticDocs(
                    gson.fromJson(body, AddStataRsDto.class).getStatisticDocs()
                );
            }
        }).runAndSetFail(res);
        return res;
    }

    @Override
    public GetStataPtrnsRsDto getStatisticForPatterns(final IdsDto patternIds) {
        HttpPost rq = new HttpPost(full("statistic/patterns/usage"));
        BasicClientDocApi.addJsonHeaderTo(rq);
        GetStataPtrnsRsDto res = new GetStataPtrnsRsDto();
        new ExceptionCatcher.IOCatcher<>(() -> {
            StringEntity entity = new StringEntity(gson.toJson(patternIds));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to get statistic for patterns '%s'", patternIds),
                HttpStatus.SC_OK
            );
            if (rsp.isPresent()) {
                String body = EntityUtils.toString(rsp.get().getEntity());
                log.info(body);
                res.setPtrnsStatas(
                    gson.fromJson(body, GetStataPtrnsRsDto.class).getPtrnsStatas()
                );
            }
        }).runAndSetFail(res);
        return res;
    }

    @Override
    public GetStataMergedPtrnsRsDto getStatisticMergedForPatterns(final IdsDto patternIds) {
        HttpPost rq = new HttpPost(full("statistic/patterns/usage/merged"));
        BasicClientDocApi.addJsonHeaderTo(rq);
        GetStataMergedPtrnsRsDto res = new GetStataMergedPtrnsRsDto();
        try {
            StringEntity entity = new StringEntity(gson.toJson(patternIds));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to get merged statistic for patterns '%s'", patternIds),
                HttpStatus.SC_OK
            );
            if (rsp.isPresent()) {
                String body = EntityUtils.toString(rsp.get().getEntity());
                log.info(body);
                res = gson.fromJson(body, GetStataMergedPtrnsRsDto.class);
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    @Override
    public GetStataDocRsDto getStatisticForDoc(final DocIdDto docIdDto) {
        HttpPost rq = new HttpPost(full("statistic/doc/usage"));
        BasicClientDocApi.addJsonHeaderTo(rq);
        GetStataDocRsDto res = new GetStataDocRsDto();
        new ExceptionCatcher.IOCatcher<>(() -> {
            StringEntity entity = new StringEntity(gson.toJson(docIdDto));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to get statistic for document '%s'", docIdDto.getDocId()),
                HttpStatus.SC_OK
            );
            if (rsp.isPresent()) {
                String body = EntityUtils.toString(rsp.get().getEntity());
                log.info(body);
                res.setDocStatas(
                    gson.fromJson(body, GetStataDocRsDto.class).getDocStatas()
                );
            }
        }).runAndSetFail(res);
        return res;
    }

    @Override
    public GetStataMergedDocRsDto getStatisticUsageMergedForDoc(final DocIdDto docDto) {
        HttpPost rq = new HttpPost(full("statistic/doc/usage/merged"));
        BasicClientDocApi.addJsonHeaderTo(rq);
        GetStataMergedDocRsDto res = new GetStataMergedDocRsDto();
        try {
            StringEntity entity = new StringEntity(gson.toJson(docDto));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format(
                    "Failed to get merged statistic for document '%s'", docDto.getDocId()
                ),
                HttpStatus.SC_OK
            );
            if (rsp.isPresent()) {
                String body = EntityUtils.toString(rsp.get().getEntity());
                log.info(body);
                res = gson.fromJson(body, GetStataMergedDocRsDto.class);
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    @Override
    public GetDownloadDocsRsDto getDownloadsCountForDocs(final IdsDto docIds) {
        HttpPost rq = new HttpPost(full("statistic/docs/downloads/count"));
        BasicClientDocApi.addJsonHeaderTo(rq);
        GetDownloadDocsRsDto res = new GetDownloadDocsRsDto();
        try {
            StringEntity entity = new StringEntity(gson.toJson(docIds));
            rq.setEntity(entity);
            Optional<HttpResponse> rsp = new RqExecution(httpClient, rq).execAnSetStatus(
                res,
                String.format("Failed to get statistic about downloads for documents '%s'", docIds),
                HttpStatus.SC_OK
            );
            if (rsp.isPresent()) {
                String body = EntityUtils.toString(rsp.get().getEntity());
                log.info(body);
                res = gson.fromJson(body, GetDownloadDocsRsDto.class);
            }
        } catch (IOException exc) {
            log.error("Failed to convert entity", exc);
            res.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
        return res;
    }

    private String full(String uri) {
        return String.format("%s%s", base, uri);
    }

}
