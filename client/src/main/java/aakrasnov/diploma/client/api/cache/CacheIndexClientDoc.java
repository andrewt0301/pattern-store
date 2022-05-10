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
import aakrasnov.diploma.client.utils.PathConverter;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.cache.DocValidityCheckRsDto;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

// TODO: Investigate partially upload from local cache and the rest from the server.
//  In general, it is possible to implement it, but is it necessary?
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
            Optional<GetDocRsDto> parsedCached = tryGetDocFromCache(docInfo.get());
            if (parsedCached.isPresent()) {
                return parsedCached.get();
            }
            failForActualDoc = true;
        }
        if (failForActualDoc) {
            return docApi.getDocFromCommon(id);
        }
        DocValidityCheckRsDto resCheck = docApi.checkDocValidityByTimestampFromCommon(
            id, docInfo.get().getDocTimestamp()
        );
        return processCheckValidity(docInfo.get(), resCheck);
    }

    @Override
    public DocsRsDto filterDocsFromCommon(final List<Filter> filters) {
        return docApi.filterDocsFromCommon(filters);
    }

    @Override
    public DocValidityCheckRsDto checkDocValidityByTimestampFromCommon(
        final String id, final String timestamp
    ) {
        return docApi.checkDocValidityByTimestampFromCommon(id, timestamp);
    }

    @Override
    public DocsRsDto getAllDocsFromCommon() {
        return docApi.getAllDocsFromCommon();
    }

    @Override
    public GetDocRsDto getDoc(final String id, final User user) {
        Optional<CachedDocInfo> docInfo = index.getCachedDocInfo(id);
        if (!docInfo.isPresent()) {
            return docApi.getDoc(id, user);
        }
        boolean failForActualDoc = false;
        if (new ActualDoc(docInfo.get()).isActual()) {
            Optional<GetDocRsDto> parsedCached = tryGetDocFromCache(docInfo.get());
            if (parsedCached.isPresent()) {
                return parsedCached.get();
            }
            failForActualDoc = true;
        }
        if (failForActualDoc) {
            return docApi.getDoc(id, user);
        }
        DocValidityCheckRsDto resCheck = docApi.checkDocValidityByTimestamp(
            id, docInfo.get().getDocTimestamp(), user
        );
        return processCheckValidity(docInfo.get(), resCheck);
    }

    @Override
    public RsBaseDto deleteById(final String id, final User user) {
        index.invalidateByDocId(id);
        return docApi.deleteById(id, user);
    }

    @Override
    public AddDocRsDto add(final DocDto document, final User user) {
        AddDocRsDto res = docApi.add(document, user);
        if (res.getStatus() == HttpStatus.SC_CREATED) {
            index.cacheDocs(Collections.singletonList(document));
        }
        return res;
    }

    @Override
    public UpdateDocRsDto update(final String id, final DocDto docUpd, final User user) {
        index.invalidateByDocId(id);
        UpdateDocRsDto res = docApi.update(id, docUpd, user);
        if (res.getStatus() == HttpStatus.SC_OK) {
            index.cacheDocs(Collections.singletonList(res.getDocDto()));
        }
        return res;
    }

    @Override
    public DocValidityCheckRsDto checkDocValidityByTimestamp(
        final String id, final String timestamp, final User user
    ) {
        return docApi.checkDocValidityByTimestamp(id, timestamp, user);
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

    private Optional<GetDocRsDto> tryGetDocFromCache(CachedDocInfo docInfo) {
        try {
            GetDocRsDto res = new GetDocRsDto();
            DocDto docDto = new PathConverter(
                Paths.get(index.getPrefix(), docInfo.getPath())
            ).toDocDto();
            res.setDocDto(docDto);
            res.setStatus(HttpStatus.SC_OK);
            return Optional.of(res);
        } catch (IOException exc) {
            log.error(
                String.format(
                    "Failed to get document '%s' from cache. It will try to get doc from server",
                    docInfo.getDocId()
                ),
                exc
            );
            index.invalidateByDocId(docInfo.getDocId());
        }
        return Optional.empty();
    }

    private GetDocRsDto processCheckValidity(
        CachedDocInfo docInfo, DocValidityCheckRsDto resCheck
    ) {
        GetDocRsDto res = new GetDocRsDto();
        String id = docInfo.getDocId();
        if (resCheck.getServerAnswer() == DocValidityCheckRsDto.ServerAnswer.EXIST_ONLY_BY_ID) {
            index.invalidateByDocId(id);
            index.cacheDocs(Collections.singletonList(resCheck.getDocDto()));
            log.info("Refreshed cache value for doc with id '{}'", id);
            res.setStatus(HttpStatus.SC_OK);
            res.setDocDto(resCheck.getDocDto());
        } else if (resCheck.getServerAnswer() == DocValidityCheckRsDto.ServerAnswer.NOT_EXIST) {
            log.warn("Document with id '{}' does not exist on the server. Read from cache", id);
            Optional<GetDocRsDto> parsedCached = tryGetDocFromCache(docInfo);
            if (parsedCached.isPresent()) {
                res = parsedCached.get();
                res.setStatus(HttpStatus.SC_OK);
            } else {
                index.invalidateByDocId(id);
                res.setStatus(HttpStatus.SC_NOT_FOUND);
            }
        } else if (resCheck.getServerAnswer() == DocValidityCheckRsDto.ServerAnswer.EXIST_BY_ID_AND_TIMESTAMP) {
            Optional<GetDocRsDto> parsedCached = tryGetDocFromCache(docInfo);
            if (parsedCached.isPresent()) {
                res = parsedCached.get();
                res.setStatus(HttpStatus.SC_OK);
            } else {
                log.warn(
                    "Failed to parse cached document with id '{}'. Take from server response", id
                );
                index.invalidateByDocId(id);
                res.setStatus(resCheck.getStatus());
                res.setMsg(resCheck.getMsg());
                res.setDocDto(resCheck.getDocDto());
            }
        } else {
            String error = String.format("Failed to found document with id '%s'", id);
            log.error(error);
            res.setMsg(error);
            res.setStatus(HttpStatus.SC_NOT_FOUND);
        }
        log.debug(res.toString());
        return res;
    }
}
