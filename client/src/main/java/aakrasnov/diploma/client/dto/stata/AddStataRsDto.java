package aakrasnov.diploma.client.dto.stata;

import aakrasnov.diploma.client.dto.RsBaseDto;
import aakrasnov.diploma.common.StatisticDto;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AddStataRsDto extends RsBaseDto {
    private List<StatisticDto> statistics;
}
