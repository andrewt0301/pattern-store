package aakrasnov.diploma.service.dto.stata;

import aakrasnov.diploma.service.dto.StatusBaseDto;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetStataMergedDocRsDto extends StatusBaseDto {
    private String docId;

    private Map<String, Integer> success;

    private Map<String, Integer> failure;

    private Map<String, Integer> download;
}
