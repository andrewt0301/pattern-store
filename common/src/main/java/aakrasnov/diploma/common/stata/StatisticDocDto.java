package aakrasnov.diploma.common.stata;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatisticDocDto implements Serializable {
    private String id;

    private String documentId;

    private StatisticPtrnsDto stataPtrns;
}
