package aakrasnov.diploma.common.stata;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageDto implements Serializable {
    /**
     * Counter of usage of a specific pattern.
     */
    private int count;

    @NonNull
    private String patternId;

    private Map<String, String> meta;
}
