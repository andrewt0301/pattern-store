package aakrasnov.diploma.service.domain;

import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString(of = {"id", "timestamp"})
@Document("patterns")
public class Pattern {
    private String id;
    private Date timestamp;
    private User author;
    private Map<String, String> meta;
    private Map<String, Object> data;
}
