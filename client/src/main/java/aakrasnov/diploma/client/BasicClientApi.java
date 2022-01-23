package aakrasnov.diploma.client;

import aakrasnov.diploma.client.filter.Filter;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Optional;
import org.apache.http.client.HttpClient;

final class BasicClientApi implements ClientApi {
    private final HttpClient httpClient;

    BasicClientApi(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<JsonObject> document(final long id) {
        // http-client GET request
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public Collection<JsonObject> filteredDocuments(final Filter filter) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public Collection<JsonObject> filteredDocuments(final Collection<Filter> filters) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void delete(final long id) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void add(final JsonObject document) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void update(final long id, final JsonObject altered) {
        throw new RuntimeException("not implemented yet");
    }
}
