package aakrasnov.diploma.service.repo.stata;

import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.dto.stata.DownloadDocDb;
import java.util.List;
import java.util.Set;

public interface CustomStatisticDocRepo {
    List<DownloadDocDb> getDownloadsCountForDocs(Set<String> ids);

    List<StatisticDoc> getStatisticForDoc(String docId);
}
