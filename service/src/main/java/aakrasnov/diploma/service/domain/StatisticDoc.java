package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.stata.StatisticDto;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(DocumentNames.STATISTIC_DOCS)
public class StatisticDoc implements Serializable {
    @Id
    private String id;

    private String documentId;

    private StatisticPtrns stataPtrns;

    public static StatisticDoc fromDto(StatisticDto dto) {
        StatisticDoc stata = new StatisticDoc();
        stata.setId(dto.getId());
        stata.setDocumentId(dto.getDocumentId());
        StatisticPtrns stataPtrns = new StatisticPtrns();
        stataPtrns.setSuccess(Usage.toListUsage(dto.getSuccess()));
        stataPtrns.setFailure(Usage.toListUsage(dto.getFailure()));
        stataPtrns.setDownload(Usage.toListUsage(dto.getDownload()));
        stata.setStataPtrns(stataPtrns);
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
