package aakrasnov.diploma.service.config.dbmigrations;

import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.domain.Usage;
import aakrasnov.diploma.service.repo.stata.StatisticDocRepo;
import aakrasnov.diploma.service.repo.stata.StatisticPtrnsRepo;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import java.util.Arrays;
import org.bson.types.ObjectId;

/**
 * Create the initial database setup for documents with statistic.
 */
@ChangeLog(order = "002")
public class DatabaseStatisticChangeLog {

    private static final String STATISTIC_PTRNS_1_ID = "789456123bbaaddcceeff123";

    private static final String STATISTIC_PTRNS_2_ID = "345634563456abcdabcdabcd";

    private static final String STATISTIC_PTRNS_3_ID = "1278872112788721bbccddaa";

    private static final String STATISTIC_PTRNS_4_ID = "dffdaaaaaeeeee1111222233";

    @ChangeSet(order = "001", id = "init_statistic_doc", author = "genryxy")
    public void initDocsStata(StatisticDocRepo stataDocRepo) {
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId(new ObjectId(DatabaseInitChangeLog.DOC_ID))
                .stataPtrns(getPtrnsStata(STATISTIC_PTRNS_1_ID, 10))
                .build()
        );
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId(new ObjectId(DatabaseInitChangeLog.DOC_ID))
                .stataPtrns(getPtrnsStata(STATISTIC_PTRNS_2_ID, 5))
                .build()
        );
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId(new ObjectId("11335577992244668800abcd"))
                .stataPtrns(getPtrnsStata(STATISTIC_PTRNS_3_ID, 7))
                .build()
        );
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId(new ObjectId("11335577992244668800abcd"))
                .stataPtrns(getPtrnsStata(STATISTIC_PTRNS_4_ID, 12))
                .build()
        );
    }

    @ChangeSet(order = "002", id = "init_statistic_patterns", author = "genryxy")
    public void initPatternsStata(StatisticPtrnsRepo stataPtrnsRepo) {
        stataPtrnsRepo.save(getPtrnsStata(STATISTIC_PTRNS_1_ID, 10));
        stataPtrnsRepo.save(getPtrnsStata(STATISTIC_PTRNS_2_ID, 5));
        stataPtrnsRepo.save(getPtrnsStata(STATISTIC_PTRNS_3_ID, 7));
        stataPtrnsRepo.save(getPtrnsStata(STATISTIC_PTRNS_4_ID, 12));
    }

    private static StatisticPtrns getPtrnsStata(String id, int succcessPtrnOne) {
        StatisticPtrns stata = new StatisticPtrns();
        stata.setId(new ObjectId(id));
        stata.setSuccess(
            Arrays.asList(
                new Usage(DatabaseInitChangeLog.PATTERN_1_ID, succcessPtrnOne),
                new Usage(DatabaseInitChangeLog.PATTERN_2_ID, 2),
                new Usage(DatabaseInitChangeLog.PATTERN_3_ID, 17)
            )
        );
        stata.setFailure(
            Arrays.asList(
                new Usage(DatabaseInitChangeLog.PATTERN_1_ID, 3),
                new Usage(DatabaseInitChangeLog.PATTERN_2_ID, 7),
                new Usage(DatabaseInitChangeLog.PATTERN_3_ID, 6)
            )
        );
        stata.setDownload(
            Arrays.asList(
                new Usage(DatabaseInitChangeLog.PATTERN_1_ID, 1),
                new Usage(DatabaseInitChangeLog.PATTERN_2_ID, 1),
                new Usage(DatabaseInitChangeLog.PATTERN_3_ID, 1),
                new Usage(DatabaseInitChangeLog.PATTERN_4_ID, 1)
            )
        );
        return stata;
    }

}
