package aakrasnov.diploma.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"id", "lang"})
@AllArgsConstructor
@NoArgsConstructor
public class DocDto implements Serializable {
    private String id;

    private String lang;

    private ScenarioDto scenario;

    private TeamDto team;

    private Date timestamp;

    private List<PatternDto> patterns;
}
