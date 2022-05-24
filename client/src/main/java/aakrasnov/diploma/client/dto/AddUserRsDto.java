package aakrasnov.diploma.client.dto;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.UserDto;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AddUserRsDto  extends RsBaseDto implements Serializable {
    private UserDto userDto;
}
