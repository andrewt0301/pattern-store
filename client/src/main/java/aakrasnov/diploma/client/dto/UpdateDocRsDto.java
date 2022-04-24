package aakrasnov.diploma.client.dto;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.RsBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UpdateDocRsDto extends RsBaseDto {
    private DocDto docDto;
}

