package aakrasnov.diploma.service.config.dbmigrations;

import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.domain.Usage;
import aakrasnov.diploma.service.repo.stata.StatisticDocRepo;
import aakrasnov.diploma.service.repo.stata.StatisticPtrnsRepo;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import java.util.Arrays;

/**
 * Create the initial database setup for documents with statistic.
 */
@ChangeLog(order = "002")
public class DatabaseStatisticChangeLog {

    private static final String STATISTIC_PTRNS_ID = "789456123bbaaddcceeff123";

    private static final String STATISTIC_PTRNS_ID_2 = "345634563456abcdabcdabcd";

    private static final String STATISTIC_PTRNS_ID_3 = "1278872112788721bbccddaa";

    private static final String STATISTIC_PTRNS_ID_4 = "dffdaaaaaeeeee1111222233";

    @ChangeSet(order = "001", id = "init_statistic_doc", author = "genryxy")
    public void initDocsStata(StatisticDocRepo stataDocRepo) {
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId(DatabaseInitChangeLog.DOC_ID)
                .stataPtrns(getPtrnsStata(STATISTIC_PTRNS_ID, 10))
                .build()
        );
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId(DatabaseInitChangeLog.DOC_ID)
                .stataPtrns(getPtrnsStata(STATISTIC_PTRNS_ID_2, 5))
                .build()
        );
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId("11335577992244668800abcd")
                .stataPtrns(getPtrnsStata(STATISTIC_PTRNS_ID_3, 7))
                .build()
        );
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId("11335577992244668800abcd")
                .stataPtrns(getPtrnsStata(STATISTIC_PTRNS_ID_4, 12))
                .build()
        );
    }

    @ChangeSet(order = "002", id = "init_statistic_patterns", author = "genryxy")
    public void initPatternsStata(StatisticPtrnsRepo stataPtrnsRepo) {
        stataPtrnsRepo.save(getPtrnsStata(STATISTIC_PTRNS_ID, 10));
        stataPtrnsRepo.save(getPtrnsStata(STATISTIC_PTRNS_ID_2, 5));
        stataPtrnsRepo.save(getPtrnsStata(STATISTIC_PTRNS_ID_3, 7));
        stataPtrnsRepo.save(getPtrnsStata(STATISTIC_PTRNS_ID_4, 12));
    }

    private static StatisticPtrns getPtrnsStata(String id, int succcessPtrnOne) {
        StatisticPtrns stata = new StatisticPtrns();
        stata.setId(id);
        stata.setSuccess(
            Arrays.asList(
                new Usage("ptrn_1", succcessPtrnOne),
                new Usage("ptrn_2", 2),
                new Usage("ptrn_3", 17)
            )
        );
        stata.setFailure(
            Arrays.asList(
                new Usage("ptrn_1", 3),
                new Usage("ptrn_2", 7),
                new Usage("ptrn_3", 6)
            )
        );
        stata.setDownload(
            Arrays.asList(
                new Usage("ptrn_1", 1),
                new Usage("ptrn_2", 1),
                new Usage("ptrn_3", 1)
            )
        );
        return stata;
    }

}
