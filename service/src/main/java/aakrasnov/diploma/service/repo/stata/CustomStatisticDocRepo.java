package aakrasnov.diploma.service.repo.stata;

import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.dto.stata.DownloadDocDb;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;

public interface CustomStatisticDocRepo {
    List<DownloadDocDb> getDownloadsCountForDocs(Set<ObjectId> ids);

    List<StatisticDoc> getStatisticForDoc(ObjectId docId);
}
