package aakrasnov.diploma.client.utils;

import aakrasnov.diploma.common.DocDto;
import java.time.LocalDateTime;

public class TimeConverter {
    private final String time;

    public TimeConverter(final String time) {
        this.time = time;
    }

    public TimeConverter(final LocalDateTime time) {
        this(time.format(DocDto.DATE_FORMATTER));
    }

    public LocalDateTime asLocalDateTime() {
        return LocalDateTime.parse(
            time, DocDto.DATE_FORMATTER
        );
    }

    public String asString() {
        return time;
    }
}
