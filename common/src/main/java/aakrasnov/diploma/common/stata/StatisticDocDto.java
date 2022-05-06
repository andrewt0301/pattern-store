package aakrasnov.diploma.common.stata;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatisticDocDto implements Serializable {
    private String id;

    @NonNull
    private String documentId;

    @NonNull
    private StatisticPtrnsDto stataPtrns;
}
