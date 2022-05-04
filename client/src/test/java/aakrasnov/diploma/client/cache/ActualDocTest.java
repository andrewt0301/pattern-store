package aakrasnov.diploma.client.cache;

import aakrasnov.diploma.common.DocDto;
import java.time.LocalDateTime;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ActualDocTest {
    @ParameterizedTest
    @CsvSource(
        value = {
            "30,true",
            "100000,false"
        }
    )
    void getActualWhenPassedLessThanOneHour(int passedMins, boolean isActual) {
        MatcherAssert.assertThat(
            new ActualDoc(
                CachedDocInfo.builder()
                    .cachingTime(
                        LocalDateTime.now()
                            .minusMinutes(passedMins)
                            .format(DocDto.DATE_FORMATTER)
                    ).build()
            ).isActual(),
            new IsEqual<>(isActual)
        );
    }
}
