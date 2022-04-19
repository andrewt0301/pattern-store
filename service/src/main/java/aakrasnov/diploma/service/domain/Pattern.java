package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.PatternDto;
import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id", "timestamp"})
@Document(DocumentNames.PATTERN)
public class Pattern {
    @MongoId(targetType = FieldType.STRING)
    private String id;

    private Date timestamp;

    private String authorId;

    private Map<String, String> meta;

    private Map<String, ? extends Object> data;

    public static PatternDto toDto(Pattern pattern) {
        PatternDto dto = new PatternDto();
        dto.setId(pattern.getId());
        dto.setTimestamp(pattern.getTimestamp());
        dto.setAuthorId(pattern.getAuthorId());
        dto.setMeta(pattern.getMeta());
        dto.setData(pattern.getData());
        return dto;
    }

    public static Pattern fromDto(PatternDto dto) {
        Pattern pattern = new Pattern();
        pattern.setId(dto.getId());
        pattern.setTimestamp(dto.getTimestamp());
        pattern.setAuthorId(dto.getAuthorId());
        pattern.setMeta(dto.getMeta());
        pattern.setData(dto.getData());
        return pattern;
    }
}
