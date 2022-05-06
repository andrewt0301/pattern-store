package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.ScenarioDto;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"type"})
@AllArgsConstructor
@NoArgsConstructor
public class Scenario {
    private Type type;
    private Map<String, String> meta;

    public enum Type {
        MIGRATION, REFACTORING, UNKNOWN, FOR_TEST
    }

    public static ScenarioDto toDto(Scenario scenario) {
        ScenarioDto dto = new ScenarioDto();
        dto.setType(ScenarioDto.Type.valueOf(scenario.getType().name()));
        dto.setMeta(scenario.getMeta());
        return dto;
    }

    public static Scenario fromDto(ScenarioDto dto) {
        Scenario scenario = new Scenario();
        scenario.setMeta(dto.getMeta());
        scenario.setType(Type.valueOf(dto.getType().name()));
        return scenario;
    }
}
