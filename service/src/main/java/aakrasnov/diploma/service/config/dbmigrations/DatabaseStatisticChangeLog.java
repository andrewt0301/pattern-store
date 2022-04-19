package aakrasnov.diploma.service.config.dbmigrations;

import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.domain.Usage;
import aakrasnov.diploma.service.repo.stata.StatisticDocRepo;
import aakrasnov.diploma.service.repo.stata.StatisticPtrnsRepo;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create the initial database setup for documents with statistic.
 */
@ChangeLog(order = "002")
public class DatabaseStatisticChangeLog {

    private static final String STATISTIC_PTRNS_ID = "789456123bbaaddcceeff123";

    @ChangeSet(order = "001", id = "init_statistic_doc", author = "genryxy")
    public void initDocsStata(StatisticDocRepo stataDocRepo) {
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId(DatabaseInitChangeLog.DOC_ID)
                .stataPtrns(getPtrnsStata())
                .build()
        );
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId(DatabaseInitChangeLog.DOC_ID)
                .stataPtrns(getPtrnsStata())
                .build()
        );
        stataDocRepo.save(
            StatisticDoc.builder()
                .documentId("11335577992244668800abcd")
                .stataPtrns(getPtrnsStata())
                .build()
        );
    }

    @ChangeSet(order = "002", id = "init_statistic_patterns", author = "genryxy")
    public void initPatternsStata(StatisticPtrnsRepo stataPtrnsRepo) {
        StatisticPtrns stataPtrns = getPtrnsStata();
        List<Usage> upd = new ArrayList<>(stataPtrns.getDownload());
        upd.add(new Usage("ptrn_extra", 1));
        stataPtrns.setDownload(upd);
        stataPtrns.setId("125693254879aabbccddeeff");
        stataPtrnsRepo.save(stataPtrns);
        stataPtrnsRepo.save(getPtrnsStata());
    }

    private static StatisticPtrns getPtrnsStata() {
        StatisticPtrns stata = new StatisticPtrns();
        stata.setId(STATISTIC_PTRNS_ID);
        stata.setSuccess(
            Arrays.asList(
                new Usage("ptrn_1", 10),
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
