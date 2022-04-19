package aakrasnov.diploma.service.dto.stata;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class DocIdsDto implements Serializable {
    private Set<String> docIds;
}
