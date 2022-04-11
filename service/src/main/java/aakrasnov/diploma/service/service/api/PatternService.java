package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.common.PatternDto;
import java.util.Optional;

public interface PatternService {
    /**
     * Obtain pattern by id.
     * @param id Id of the pattern which should be received
     * @return Pattern if it exists.
     */
    Optional<PatternDto> getById(final String id);

    /**
     * Add a new team to the database.
     * @param patternDto Pattern which should be added
     * @return Added pattern.
     */
    PatternDto addPattern(final PatternDto patternDto);
}
