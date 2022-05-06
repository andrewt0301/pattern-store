package aakrasnov.diploma.client.api.cache;

import aakrasnov.diploma.client.api.ClientDocApi;
import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.AddDocRsDto;
import aakrasnov.diploma.client.dto.DocsRsDto;
import aakrasnov.diploma.client.dto.GetDocRsDto;
import aakrasnov.diploma.client.dto.UpdateDocRsDto;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.common.RsBaseDto;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// TODO: Implement partially upload from local cache and the rest from the server.
public class CacheIndexBasicClientDoc implements ClientDocApi {

    private final ClientDocApi docApi;

    private final Path pathToCache;

    public CacheIndexBasicClientDoc(final ClientDocApi docApi) {
        this(docApi, Paths.get("."));
    }

    public CacheIndexBasicClientDoc(final ClientDocApi docApi, final Path pathToCache) {
        this.docApi = docApi;
        this.pathToCache = pathToCache;
    }

    @Override
    public GetDocRsDto getDocFromCommon(final String id) {
        // TODO: check in cache
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
