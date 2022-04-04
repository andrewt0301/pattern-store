package aakrasnov.diploma.client;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import java.util.List;
import java.util.Optional;
import org.apache.http.client.HttpClient;

final class BasicClientApi implements ClientApi {
    private final HttpClient httpClient;

    BasicClientApi(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<DocDto> document(final long id) {
        // http-client GET request
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public List<DocDto> filteredDocuments(final Filter filter) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public List<DocDto> filteredDocuments(final List<Filter> filters) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void deleteById(final long id) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void add(final DocDto document) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void update(final long id, final DocDto altered) {
        throw new RuntimeException("not implemented yet");
    }
}
