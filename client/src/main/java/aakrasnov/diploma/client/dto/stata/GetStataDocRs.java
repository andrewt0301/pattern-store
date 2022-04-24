package aakrasnov.diploma.client.dto.stata;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.stata.StatisticDocDto;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetStataDocRs extends RsBaseDto implements Serializable {
    private List <StatisticDocDto> docStatas;
}
