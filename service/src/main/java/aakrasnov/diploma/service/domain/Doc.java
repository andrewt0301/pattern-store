package aakrasnov.diploma.service.domain;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString(of = {"id", "lang"})
@Document("docs")
public class Doc {
    @Id
    private String id;
    private String lang;
    private Scenario scenario;

    private List<Pattern> patterns;
}
