package aakrasnov.diploma.common;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class StatisticDto implements Serializable {
    private String documentId;
    private Map<String, UsageDto> success;
    private Map<String, UsageDto> failure;
    private Map<String, UsageDto> download;
}
