package aakrasnov.diploma.client.dto.stata;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.stata.StatisticPtrnsDto;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetStataPtrnsRs extends RsBaseDto {
    private List<StatisticPtrnsDto> ptrnsStatas;
}
