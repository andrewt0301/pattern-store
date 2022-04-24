package aakrasnov.diploma.common.stata;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class StatisticPtrnsDto implements Serializable {
    private String id;

    private List<UsageDto> success;

    private List<UsageDto> failure;

    private List<UsageDto> download;
}
