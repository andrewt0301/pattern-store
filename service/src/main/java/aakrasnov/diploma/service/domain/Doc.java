package aakrasnov.diploma.service.domain;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@ToString(of = {"id", "lang"})
@RequiredArgsConstructor
@AllArgsConstructor
@Document("docs")
public class Doc {
    @Id
    private String id;

    @NonNull
    private String lang;

    @NonNull
    private Scenario scenario;

    @NonNull
    private String authorId;

    private Date timestamp = Date.from(Instant.now());

    // Contains only ID to some patterns (e.g. FK to the pattern)
    @NonNull
    private List<String> patterns;
}
