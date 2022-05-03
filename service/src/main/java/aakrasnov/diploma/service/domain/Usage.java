package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.stata.UsageDto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@ToString
public class Usage implements Serializable {
    private ObjectId patternId;
    private Integer count;
    private Map<String, String> meta;

    public Usage(String patternId, int count) {
        this(new ObjectId(patternId), count, new HashMap<>());
    }

    public static Usage fromDto(UsageDto dto) {
        Usage usage = new Usage();
        usage.setPatternId(new ObjectId(dto.getPatternId()));
        usage.setCount(dto.getCount());
        usage.setMeta(dto.getMeta());
        return usage;
    }

    public static UsageDto toDto(Usage usage) {
        UsageDto dto = new UsageDto();
        dto.setPatternId(usage.getPatternId().toHexString());
        dto.setCount(usage.getCount());
        dto.setMeta(usage.getMeta());
        return dto;
    }

    public static List<Usage> toListUsage(List<UsageDto> dtos) {
        return Optional.ofNullable(dtos).map(
            vals -> vals.stream()
            .map(Usage::fromDto)
            .collect(Collectors.toList())
        ).orElse(new ArrayList<>());
    }

    public static List<UsageDto> toListUsageDto(List<Usage> usages) {
        return Optional.ofNullable(usages).map(
            vals -> vals.stream()
            .map(Usage::toDto)
            .collect(Collectors.toList())
        ).orElse(new ArrayList<>());
    }

//    public static Map<String, Usage> toMapUsage(Map<String, UsageDto> entries) {
//        return applyUsageConverter(entries, Usage::fromDto);
//    }
//
//    public static Map<String, UsageDto> toMapUsageDto(Map<String, Usage> entries) {
//        return applyUsageConverter(entries, Usage::toDto);
//    }

//    private static <T, R> Map<String, R> applyUsageConverter(
//        Map<String, T> entries, Function<T, R> func
//    ) {
//        return entries.entrySet()
//            .stream()
//            .collect(
//                Collectors.toMap(
//                    Map.Entry::getKey, val -> func.apply(val.getValue())
//                )
//            );
//    }
}
