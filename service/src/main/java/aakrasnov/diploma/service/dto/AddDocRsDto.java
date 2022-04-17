package aakrasnov.diploma.service.dto;

import aakrasnov.diploma.service.domain.Doc;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddDocRsDto extends StatusBaseDto implements Serializable {
    private Doc doc;
}
