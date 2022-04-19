package aakrasnov.diploma.service.dto.stata;

import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.dto.StatusBaseDto;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetStataPtrnsRsDto extends StatusBaseDto implements Serializable {
    private List<StatisticPtrns> ptrnsStatas;
}
