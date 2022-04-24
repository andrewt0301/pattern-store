package aakrasnov.diploma.common.stata;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocIdDto implements Serializable {
    private String docId;
}
