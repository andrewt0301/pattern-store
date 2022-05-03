package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.stata.StatisticPtrnsDto;
import aakrasnov.diploma.common.stata.UsageDto;
import aakrasnov.diploma.service.json.StatisticPtrnsDeserializer;
import aakrasnov.diploma.service.json.UsageDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Database document for storing statistic about usage of patterns.
 */
@Data
@Document(DocumentNames.STATISTIC_PTRNS)
public class StatisticPtrns implements Serializable {
    @Id
    private ObjectId id;

    private List<Usage> success;

    private List<Usage> failure;

    private List<Usage> download;

    public static StatisticPtrns fromJson(String json) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(StatisticPtrns.class, new StatisticPtrnsDeserializer())
            .registerTypeAdapter(Usage.class, new UsageDeserializer())
            .create();
        return gson.fromJson(json, StatisticPtrns.class);
    }

    public static StatisticPtrnsDto toDto(StatisticPtrns stata) {
        StatisticPtrnsDto dto = new StatisticPtrnsDto();
        dto.setId(stata.getId().toHexString());
        dto.setSuccess(toUsageDtos(stata.getSuccess()));
        dto.setFailure(toUsageDtos(stata.getFailure()));
        dto.setDownload(toUsageDtos(stata.getDownload()));
        return dto;
    }

    private static List<UsageDto> toUsageDtos(List<Usage> usages) {
        return Optional.ofNullable(usages)
            .map(
                vals -> vals.stream().map(Usage::toDto)
                    .collect(Collectors.toList())
            ).orElse(new ArrayList<>());
    }
}
