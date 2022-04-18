package aakrasnov.diploma.common;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class UsageDto implements Serializable {
    /**
     * Counter of usage of a specific pattern.
     */
    private int count;
    private Map<String, String> meta;
}
