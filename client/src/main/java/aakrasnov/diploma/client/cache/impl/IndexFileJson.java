package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.cache.CachedDocInfo;
import aakrasnov.diploma.client.cache.IndexFile;
import aakrasnov.diploma.client.utils.TimeConverter;
import aakrasnov.diploma.common.DocDto;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class IndexFileJson implements IndexFile {
    public static final String CACHING_TIME = "cachingTime";

    public static final String CACHE_CREATION = "cacheCreation";

    public static final String DOC_TIMESTAMP = "docTimestamp";

    public static final String DOC_PATH = "path";

    private final JsonObject json;

    public IndexFileJson() {
        this(getEmptyIndex());
    }

    public IndexFileJson(final InputStream input) {
        this(
            JsonParser.parseReader(
                new InputStreamReader(
                    new BufferedInputStream(input)
                )
            ).getAsJsonObject()
        );
    }

    public IndexFileJson(final JsonObject json) {
        this.json = json;
    }

    @Override
    public LocalDateTime getIndexTimeCreation() {
        return new TimeConverter(
            json.get(CACHE_CREATION).getAsString()
        ).asLocalDateTime();
    }

    @Override
    public Optional<CachedDocInfo> getCachedDocInfo(final String docId) {
        JsonElement elemById = json.get(docId);
        if (elemById == null) {
            return Optional.empty();
        }
        JsonObject obj = elemById.getAsJsonObject();
        return Optional.of(
            CachedDocInfo.builder()
                .docId(docId)
                .docTimestamp(obj.get(DOC_TIMESTAMP).getAsString())
                .cachingTime(obj.get(CACHING_TIME).getAsString())
                .path(obj.get(DOC_PATH).getAsString())
                .build()
        );
    }

    @Override
    public void removeDoc(final String docId) {
        json.remove(docId);
    }

    @Override
    public void cacheDocs(final List<DocDto> docs) {
        docs.forEach(
            doc -> json.add(
                doc.getId(),
                new CachedDocInfo(doc.getId(), doc.getTimestamp()).toJsonObject()
            )
        );
    }

    @Override
    public String asString() {
        return json.toString();
    }

    private static JsonObject getEmptyIndex() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(CACHE_CREATION, new TimeConverter(LocalDateTime.now()).asString());
        return jsonObj;
    }

}
