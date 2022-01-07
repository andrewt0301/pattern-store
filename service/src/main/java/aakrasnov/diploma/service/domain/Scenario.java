package aakrasnov.diploma.service.domain;

import java.util.Map;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = {"type"})
public class Scenario {
    private Type type;
    private Map<String, String> meta;

    public enum Type {
        MIGRATION
    }
}
