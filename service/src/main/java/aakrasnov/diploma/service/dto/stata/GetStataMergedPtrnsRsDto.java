package aakrasnov.diploma.service.dto.stata;

import aakrasnov.diploma.service.dto.StatusBaseDto;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetStataMergedPtrnsRsDto extends StatusBaseDto {
    private Map<String, Integer> success;

    private Map<String, Integer> failure;

    private Map<String, Integer> download;
}
