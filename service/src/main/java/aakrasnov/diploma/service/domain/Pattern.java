package aakrasnov.diploma.service.domain;

import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@ToString(of = {"id", "timestamp"})
@Document("patterns")
public class Pattern {
    private String id;
    private Date timestamp;
    private String authorId;
    private Map<String, String> meta;
    private Map<String, ? extends Object> data;
}
