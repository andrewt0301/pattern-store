package aakrasnov.diploma.common.stata;

import aakrasnov.diploma.common.RsBaseDto;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddStataRsDto extends RsBaseDto implements Serializable {
    private List<StatisticDto> statisticDocs;
}
