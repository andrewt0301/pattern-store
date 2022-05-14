package aakrasnov.diploma.client.dto.team;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.TeamDto;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AddTeamRsDto extends RsBaseDto implements Serializable {
    private TeamDto teamDto;
}
