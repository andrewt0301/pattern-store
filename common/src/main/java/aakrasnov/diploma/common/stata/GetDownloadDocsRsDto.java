package aakrasnov.diploma.common.stata;

import aakrasnov.diploma.common.RsBaseDto;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetDownloadDocsRsDto extends RsBaseDto {
    private Map<String, Integer> docsDownloads;
}
