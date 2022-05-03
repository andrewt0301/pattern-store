package aakrasnov.diploma.common.stata;

import aakrasnov.diploma.common.RsBaseDto;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetStataDocRsDto extends RsBaseDto {
    private List<StatisticDocDto> docStatas;
}
