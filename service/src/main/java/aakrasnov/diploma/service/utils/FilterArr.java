package aakrasnov.diploma.service.utils;

import aakrasnov.diploma.common.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterArr {
    private final List<Filter> filters;

    public FilterArr(final List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * Concat in one array values for the same key.
     * @return Map with combined values for keys.
     */
    public Map<String, List<String>> concatValsOfSameKeys() {
        Map<String, List<String>> res = new HashMap<>();
        for (Filter filter : filters) {
            res.putIfAbsent(filter.key(), new ArrayList<>());
            res.get(filter.key()).add(filter.value());
        }
        return res;
    }
}
