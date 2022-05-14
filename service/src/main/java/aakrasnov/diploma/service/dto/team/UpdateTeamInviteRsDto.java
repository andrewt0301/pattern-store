package aakrasnov.diploma.service.dto.team;

import aakrasnov.diploma.common.RsBaseDto;
import aakrasnov.diploma.common.TeamDto;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateTeamInviteRsDto extends RsBaseDto implements Serializable {
    private TeamDto teamDto;
}
