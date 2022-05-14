package aakrasnov.diploma.common;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString(of = {"id", "lang"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocDto implements Serializable {
    private final static String DATE_FORMAT = "yyyy:MM:dd HH:mm:ss";

    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private String id;

    private String lang;

    @NonNull
    private ScenarioDto scenario;

    private TeamDto team;

    private String timestamp;

    private List<PatternDto> patterns;
}
