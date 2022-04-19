package aakrasnov.diploma.service.dto.stata;

import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.dto.StatusBaseDto;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetStataDocRsDto extends StatusBaseDto {
    private List<StatisticDoc> docStatas;
}
