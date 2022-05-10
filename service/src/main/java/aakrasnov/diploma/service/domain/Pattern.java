package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.PatternDto;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
@ToString(of = {"id", "timestamp"})
@Document(DocumentNames.PATTERN)
public class Pattern {
    @Id
    private ObjectId id;

    private LocalDateTime timestamp;

    @NonNull
    private ObjectId authorId;

    private Map<String, String> meta;

    private Map<String, ? extends Object> data;

    public static PatternDto toDto(Pattern pattern) {
        PatternDto dto = new PatternDto();
        dto.setId(pattern.getId().toHexString());
        dto.setTimestamp(
            Optional.ofNullable(pattern.getTimestamp()).map(Objects::toString).orElse(null)
        );
        dto.setAuthorId(pattern.getAuthorId().toHexString());
        dto.setMeta(pattern.getMeta());
        dto.setData(pattern.getData());
        return dto;
    }

    public static Pattern fromDto(PatternDto dto) {
        Pattern pattern = new Pattern();
        if (dto.getId() != null) {
            pattern.setId(new ObjectId(dto.getId()));
        }
        pattern.setTimestamp(
            Optional.ofNullable(dto.getTimestamp())
                .map(LocalDateTime::parse)
                .orElse(null)
        );
        pattern.setAuthorId(new ObjectId(dto.getAuthorId()));
        pattern.setMeta(dto.getMeta());
        pattern.setData(dto.getData());
        return pattern;
    }
}
