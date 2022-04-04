package aakrasnov.diploma.common;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"type"})
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioDto implements Serializable {
    private Type type;
    private Map<String, String> meta;

    public enum Type {
        MIGRATION, REFACTORING, UNKNOWN
    }
}
