package aakrasnov.diploma.client.cache;

import aakrasnov.diploma.client.cache.impl.IndexFileJson;
import aakrasnov.diploma.client.utils.TimeConverter;
import com.google.gson.JsonObject;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CachedDocInfo implements Serializable {
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

    public CachedDocInfo(String docId, String docTimestamp, String path) {
        this(
            docId,
            docTimestamp,
            path,
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

    public static CachedDocInfo fromJsonObject(JsonObject obj) {
        CachedDocInfo docInfo = new CachedDocInfo();
        docInfo.setDocTimestamp(obj.get(IndexFileJson.DOC_TIMESTAMP).getAsString());
        docInfo.setPath(obj.get(IndexFileJson.DOC_PATH).getAsString());
        docInfo.setCachingTime(obj.get(IndexFileJson.CACHING_TIME).getAsString());
        return docInfo;
    }
}
