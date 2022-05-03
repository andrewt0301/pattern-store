package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.stata.StatisticDocDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(DocumentNames.STATISTIC_DOCS)
public class StatisticDoc implements Serializable {
    @Id
    private ObjectId id;

    @NonNull
    private ObjectId documentId;

    private StatisticPtrns stataPtrns;

    public static StatisticDoc fromDto(StatisticDto dto) {
        StatisticDoc stata = new StatisticDoc();
        if (dto.getId() != null) {
            stata.setId(new ObjectId(dto.getId()));
        }
        stata.setDocumentId(new ObjectId(dto.getDocumentId()));
        StatisticPtrns stataPtrns = new StatisticPtrns();
        stataPtrns.setSuccess(Usage.toListUsage(dto.getSuccess()));
        stataPtrns.setFailure(Usage.toListUsage(dto.getFailure()));
        stataPtrns.setDownload(Usage.toListUsage(dto.getDownload()));
        stata.setStataPtrns(stataPtrns);
        return stata;
    }

    public static StatisticDto toDto(StatisticDoc stata) {
        StatisticDto dto = new StatisticDto();
        dto.setId(stata.getId().toHexString());
        dto.setDocumentId(stata.getDocumentId().toHexString());
        dto.setSuccess(Usage.toListUsageDto(stata.getStataPtrns().getSuccess()));
        dto.setFailure(Usage.toListUsageDto(stata.getStataPtrns().getFailure()));
        dto.setDownload(Usage.toListUsageDto(stata.getStataPtrns().getDownload()));
        return dto;
    }

    public static StatisticDocDto toDocDto(StatisticDoc stata) {
        StatisticDocDto dto = new StatisticDocDto();
        dto.setDocumentId(stata.getDocumentId().toHexString());
        dto.setId(stata.getId().toHexString());
        dto.setStataPtrns(StatisticPtrns.toDto(stata.getStataPtrns()));
        return dto;
    }
}
