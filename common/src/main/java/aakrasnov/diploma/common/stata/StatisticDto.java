package aakrasnov.diploma.common.stata;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDto implements Serializable {
    private String id;

    @NonNull
    private String documentId;

    private List<UsageDto> success;

    private List<UsageDto> failure;

    private List<UsageDto> download;
}
