package aakrasnov.diploma.common.cache;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"id", "timestamp"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocValidityDto implements Serializable {
    private String id;
    private String timestamp;
}
