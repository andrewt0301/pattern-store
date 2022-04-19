package aakrasnov.diploma.service.utils;

import aakrasnov.diploma.service.service.api.StatisticService;
import com.google.common.collect.ImmutableSet;

public class MyTmpTest {
    private final StatisticService statisticService;

    public MyTmpTest(final StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    public void call() {
        // getDownloadsCountForDocs
        System.out.println(statisticService.getDownloadsCountForDocs(
            ImmutableSet.of("625748988af05121cc0d6189", "11335577992244668800abcd")
        ));

        // getStatisticForDoc
        System.out.println(
            statisticService.getStatisticForDoc("625748988af05121cc0d6189")
        );
        System.out.println("check for another doc");
        System.out.println(
            statisticService.getStatisticForDoc("11335577992244668800abcd")
        );

        // getStatisticUsageMergedForDoc
        System.out.println(
            statisticService.getStatisticUsageMergedForDoc("625748988af05121cc0d6189")
        );
        System.out.println("check for another doc");
        System.out.println(
            statisticService.getStatisticUsageMergedForDoc("11335577992244668800abcd")
        );

        // getStatisticForPatterns
        System.out.println(
            statisticService.getStatisticForPatterns(ImmutableSet.of("ptrn_1"))
        );
        System.out.println("check for other patterns");
        System.out.println(
            statisticService.getStatisticForPatterns(ImmutableSet.of("ptrn_1", "ptrn_2"))
        );

        // getStatisticMergedForPatterns
        System.out.println(
            statisticService.getStatisticMergedForPatterns(ImmutableSet.of("ptrn_1"))
        );
        System.out.println("check merged for other patterns");
        System.out.println(
            statisticService.getStatisticMergedForPatterns(ImmutableSet.of("ptrn_1", "ptrn_2"))
        );
    }
}
