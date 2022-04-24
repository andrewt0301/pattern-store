package aakrasnov.diploma.service.dto.stata;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetStataPtrnsRsDto extends RsBaseDto implements Serializable {
    private List<StatisticPtrns> ptrnsStatas;
}
