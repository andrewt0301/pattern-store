package aakrasnov.diploma.service.utils;

import aakrasnov.diploma.service.domain.Usage;
import java.util.List;
import java.util.Map;

public class MergeUsage {
    private final List<Usage> source;

    public MergeUsage(final List<Usage> source) {
        this.source = source;
    }

    public void mergeWith(Map<String, Integer> target) {
        source.forEach(
            usage -> target.compute(
                usage.getPatternId(),
                (key, val) -> (val == null) ? usage.getCount() : usage.getCount() + val
            )
        );
    }
}
