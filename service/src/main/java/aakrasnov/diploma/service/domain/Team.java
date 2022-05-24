package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.TeamDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "name"})
@Document(DocumentNames.TEAMS)
public class Team {
    public static ObjectId COMMON_TEAM_ID = new ObjectId("624f6f6b5bdddf7ee83350a0");

    @Id
    private ObjectId id;

    @NonNull
    private String name;

    @NonNull
    private ObjectId creatorId;

    private String invitation;

    public static TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();
        if (team.getId() != null) {
            dto.setId(team.getId().toHexString());
        }
        dto.setName(team.getName());
        dto.setCreatorId(team.getCreatorId().toHexString());
        dto.setInvitation(team.getInvitation());
        return dto;
    }

    public static Team fromDto(TeamDto dto) {
        Team team = new Team();
        team.setId(new ObjectId(dto.getId()));
        team.setName(dto.getName());
        team.setCreatorId(new ObjectId(dto.getCreatorId()));
        team.setInvitation(dto.getInvitation());
        return team;
    }
}
