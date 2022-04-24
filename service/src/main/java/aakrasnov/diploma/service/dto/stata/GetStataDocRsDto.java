package aakrasnov.diploma.service.dto.stata;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.service.domain.StatisticDoc;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetStataDocRsDto extends RsBaseDto {
    private List<StatisticDoc> docStatas;
}
