package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.cache.CachedDocInfo;
import aakrasnov.diploma.client.cache.IndexFile;
import aakrasnov.diploma.client.utils.TimeConverter;
import aakrasnov.diploma.common.DocDto;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexFileJson implements IndexFile {
    public static final String CACHING_TIME = "cachingTime";

    public static final String CACHE_CREATION = "cacheCreation";

    public static final String DOC_TIMESTAMP = "docTimestamp";

    public static final String DOC_PATH = "path";

    private final JsonObject json;

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
    public Optional<CachedDocInfo> removeDoc(final String docId) {
        if (json.has(docId)) {
            CachedDocInfo docInfo = CachedDocInfo.fromJsonObject(json.get(docId).getAsJsonObject());
            docInfo.setDocId(docId);
            json.remove(docId);
            return Optional.of(docInfo);
        }
        return Optional.empty();
    }

    @Override
    public void cacheDocs(final List<DocDto> docs, final List<String> docsPaths) {
        if (docs.size() != docsPaths.size()) {
            log.error(
                String.format(
                    "Failed to cache about docs. Docs: '%s', paths_of_documents: '%s'",
                    docs,
                    docsPaths
                )
            );
        }
        for (int i = 0; i < docs.size(); i++) {
            DocDto doc = docs.get(i);
            json.add(
                doc.getId(),
                new CachedDocInfo(
                    doc.getId(), doc.getTimestamp(), docsPaths.get(i)
                ).toJsonObject()
            );
        }
    }

    @Override
    public String asString() {
        return json.toString();
    }

    @Override
    public void writeTo(final OutputStream output) throws IOException {
        output.write(asString().getBytes());
    }
}
