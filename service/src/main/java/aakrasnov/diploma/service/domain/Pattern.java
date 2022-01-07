package aakrasnov.diploma.service.domain;

import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = {"id", "timestamp"})
public class Pattern {
    private String id;
    private Date timestamp;
    private User author;
    private Map<String, String> meta;
    private Map<String, Object> data;
}
