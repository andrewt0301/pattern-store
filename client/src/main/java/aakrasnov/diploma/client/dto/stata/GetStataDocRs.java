package aakrasnov.diploma.client.dto.stata;

import aakrasnov.diploma.common.stata.StatisticDto;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class GetStataDocRs implements Serializable {
    private List <StatisticDto> docStatas;
}
