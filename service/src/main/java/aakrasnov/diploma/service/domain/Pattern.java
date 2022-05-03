package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.PatternDto;
import java.util.Date;
import java.util.Map;
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

    private Date timestamp;

    @NonNull
    private ObjectId authorId;

    private Map<String, String> meta;

    private Map<String, ? extends Object> data;

    public static PatternDto toDto(Pattern pattern) {
        PatternDto dto = new PatternDto();
        dto.setId(pattern.getId().toHexString());
        dto.setTimestamp(pattern.getTimestamp());
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
        pattern.setTimestamp(dto.getTimestamp());
        pattern.setAuthorId(new ObjectId(dto.getAuthorId()));
        pattern.setMeta(dto.getMeta());
        pattern.setData(dto.getData());
        return pattern;
    }
}
