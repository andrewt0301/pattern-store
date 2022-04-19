package aakrasnov.diploma.common;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class StatisticDto implements Serializable {
    private String id;
    private String documentId;
    private List<UsageDto> success;
    private List<UsageDto> failure;
    private List<UsageDto> download;
}
