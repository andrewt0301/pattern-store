package aakrasnov.diploma.client.api.cache;

import aakrasnov.diploma.client.api.ClientDocApi;
import aakrasnov.diploma.client.cache.ActualDoc;
import aakrasnov.diploma.client.cache.CachedDocInfo;
import aakrasnov.diploma.client.cache.Index;
import aakrasnov.diploma.client.cache.impl.IndexJson;
import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.AddDocRsDto;
import aakrasnov.diploma.client.dto.DocsRsDto;
import aakrasnov.diploma.client.dto.GetDocRsDto;
import aakrasnov.diploma.client.dto.UpdateDocRsDto;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.common.RsBaseDto;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

// TODO: Implement partially upload from local cache and the rest from the server.
@Slf4j
public final class CacheIndexClientDoc implements ClientDocApi {

    private final ClientDocApi docApi;

    private final Index index;

    public CacheIndexClientDoc(final ClientDocApi docApi) {
        this(docApi, new IndexJson("index.json"));
    }

    public CacheIndexClientDoc(final ClientDocApi docApi, final Index index) {
        this.docApi = docApi;
        this.index = index;
    }

    @Override
    public GetDocRsDto getDocFromCommon(final String id) {
        Optional<CachedDocInfo> docInfo = index.getCachedDocInfo(id);
        if (!docInfo.isPresent()) {
            return docApi.getDocFromCommon(id);
        }
        boolean failForActualDoc = false;
        if (new ActualDoc(docInfo.get()).isActual()) {
            try (
                InputStream input = new FileInputStream(
                    Paths.get(index.getPrefix(), docInfo.get().getPath()).toString()
                )
            ) {
                GetDocRsDto res = new GetDocRsDto();
                DocDto docDto = new Gson().fromJson(
                    new InputStreamReader(new BufferedInputStream(input)),
                    DocDto.class
                );
                res.setDocDto(docDto);
                res.setStatus(HttpStatus.SC_OK);
                return res;
            } catch (IOException exc) {
                log.error(
                    String.format(
                        "Failed to get document '%s' from cache. It will try to get from server", id
                    ),
                    exc
                );
                index.invalidateByDocId(id);
                failForActualDoc = true;
            }
        }
        if (failForActualDoc) {
            // get from service
        }
        // compare docInfo docTimestamp
        return null;
    }

    @Override
    public DocsRsDto filterDocsFromCommon(final List<Filter> filters) {
        // TODO: check count of docs in common and compare with cache.
        return null;
    }

    @Override
    public DocsRsDto getAllDocsFromCommon() {
        return null;
    }

    @Override
    public GetDocRsDto getDoc(final String id, final User user) {
        // TODO: check in cache
        return null;
    }

    @Override
    public RsBaseDto deleteById(final String id, final User user) {
        // TODO: cache invalidate
        return docApi.deleteById(id, user);
    }

    @Override
    public AddDocRsDto add(final DocDto document, final User user) {
        // TODO: add to cache
        return docApi.add(document, user);
    }

    @Override
    public UpdateDocRsDto update(final String id, final DocDto docUpd, final User user) {
        // TODO: cache invalidate
        return docApi.update(id, docUpd, user);
    }

    @Override
    public DocsRsDto filterDocuments(final List<Filter> filters, final User user) {
        return docApi.filterDocuments(filters, user);
    }

    @Override
    public DocsRsDto getAllDocsForUser(final User user) {
        return docApi.getAllDocsForUser(user);
    }

    @Override
    public DocsRsDto getDocsByTeamId(final String teamId, final User user) {
        return docApi.getDocsByTeamId(teamId, user);
    }
}
