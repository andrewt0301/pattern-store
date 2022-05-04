package aakrasnov.diploma.client.cache;

import aakrasnov.diploma.client.utils.TimeConverter;
import java.time.Duration;
import java.time.LocalDateTime;

public class ActualDoc {
    private static final Duration NOT_CHECK_IN_DB = Duration.ofHours(1);

    private final CachedDocInfo docInfo;

    public ActualDoc(final CachedDocInfo docInfo) {
        this.docInfo = docInfo;
    }

    /**
     * Check whether entry with document is actual.
     * @return True - if current time minus caching time
     *  is less than 1 hour, otherwise - false.
     */
    public boolean isActual() {
        return new TimeConverter(docInfo.getCachingTime())
            .asLocalDateTime()
            .plus(NOT_CHECK_IN_DB)
            .isAfter(LocalDateTime.now());
    }
}
