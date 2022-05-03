package aakrasnov.diploma.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id", "name"})
public class TeamDto implements Serializable {
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String creatorId;

    private String invitation;
}
