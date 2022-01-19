package aakrasnov.diploma.service.domain;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = {"type"})
@AllArgsConstructor
public class Scenario {
    private Type type;
    private Map<String, String> meta;

    public enum Type {
        MIGRATION, REFACTORING, UNKNOWN
    }
}
