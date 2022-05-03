package aakrasnov.diploma.service.utils;

import aakrasnov.diploma.common.stata.UsageDto;
import java.util.List;
import java.util.Map;

public class MergeUsage {
    private final List<UsageDto> source;

    public MergeUsage(final List<UsageDto> source) {
        this.source = source;
    }

    public void mergeWith(Map<String, Integer> target) {
        if (target == null) {
            return;
        }
        source.forEach(
            usage -> target.compute(
                usage.getPatternId(),
                (key, val) -> (val == null) ? usage.getCount() : usage.getCount() + val
            )
        );
    }
}
