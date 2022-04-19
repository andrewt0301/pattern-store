package aakrasnov.diploma.service.dto.stata;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class PtrnIdsDto implements Serializable {
    private Set<String> ptrnIds;
}

