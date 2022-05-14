package aakrasnov.diploma.client.dto.team;

import aakrasnov.diploma.common.RsBaseDto;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeleteTeamRsDto extends RsBaseDto implements Serializable {
}
