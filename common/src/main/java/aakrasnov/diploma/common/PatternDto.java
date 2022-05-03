package aakrasnov.diploma.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id", "timestamp"})
public class PatternDto implements Serializable {
    private String id;

    private Date timestamp;

    @NonNull
    private String authorId;

    private Map<String, String> meta;

    private Map<String, ? extends Object> data;
}
