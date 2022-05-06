package aakrasnov.diploma.client.cache;

import aakrasnov.diploma.client.cache.impl.IndexFileJson;
import aakrasnov.diploma.client.utils.TimeConverter;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CachedDocInfo {
    private String docId;

    private String docTimestamp;

    private String path;

    private String cachingTime;

    public CachedDocInfo(String docId, String docTimestamp) {
        this(
            docId,
            docTimestamp,
            String.format("%s.json", docId),
            new TimeConverter(LocalDateTime.now()).asString()
        );
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(IndexFileJson.CACHING_TIME, cachingTime);
        jsonObj.addProperty(IndexFileJson.DOC_TIMESTAMP, docTimestamp);
        jsonObj.addProperty(IndexFileJson.DOC_PATH, path);
        return jsonObj;
    }
}
