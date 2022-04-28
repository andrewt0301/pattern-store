package aakrasnov.diploma.service.domain;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Database document for storing statistic about usage of patterns.
 */
@Data
@Document(DocumentNames.STATISTIC_PTRNS)
public class StatisticPtrns implements Serializable {
    @Id
    private String id;

    private List<Usage> success;

    private List<Usage> failure;

    private List<Usage> download;

    public static StatisticPtrns fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, StatisticPtrns.class);
    }
}
