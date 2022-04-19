package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.StatisticDto;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(DocumentNames.STATISTIC_DOCS)
public class StatisticDoc implements Serializable {
    @MongoId(targetType = FieldType.STRING)
    private String id;

    private String documentId;

    private StatisticPtrns stataPtrns;

    public static StatisticDoc fromDto(StatisticDto dto) {
        StatisticDoc stata = new StatisticDoc();
        stata.setId(dto.getId());
        stata.setDocumentId(dto.getDocumentId());
        stata.getStataPtrns().setSuccess(Usage.toListUsage(dto.getSuccess()));
        stata.getStataPtrns().setFailure(Usage.toListUsage(dto.getFailure()));
        stata.getStataPtrns().setDownload(Usage.toListUsage(dto.getDownload()));
        return stata;
    }

    public static StatisticDto toDto(StatisticDoc stata) {
        StatisticDto dto = new StatisticDto();
        dto.setId(stata.getId());
        dto.setDocumentId(stata.getDocumentId());
        dto.setSuccess(Usage.toListUsageDto(stata.getStataPtrns().getSuccess()));
        dto.setFailure(Usage.toListUsageDto(stata.getStataPtrns().getFailure()));
        dto.setDownload(Usage.toListUsageDto(stata.getStataPtrns().getDownload()));
        return dto;
    }
}
