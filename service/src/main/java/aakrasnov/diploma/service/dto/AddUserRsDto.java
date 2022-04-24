package aakrasnov.diploma.service.dto;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.service.domain.User;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddUserRsDto extends RsBaseDto implements Serializable {
    private User user;
}
