package aakrasnov.diploma.service.dto;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.service.domain.Doc;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddDocRsDto extends RsBaseDto implements Serializable {
    private Doc doc;
}
