package aakrasnov.diploma.client.api;

import aakrasnov.diploma.common.stata.AddStataRsDto;
import aakrasnov.diploma.common.stata.DocIdDto;
import aakrasnov.diploma.common.stata.GetDownloadDocsRsDto;
import aakrasnov.diploma.common.stata.GetStataDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedPtrnsRsDto;
import aakrasnov.diploma.common.stata.GetStataPtrnsRsDto;
import aakrasnov.diploma.common.stata.IdsDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import java.util.List;
import java.util.Set;

public interface ClientStatisticApi {
    /**
     * Send statistic about usage of pattern for the document.
     * @param statistics Statistic of usage of the document with patterns
     * @return Added statistic with status.
     */
    AddStataRsDto sendDocStatistic(List<StatisticDto> statistics);

    /**
     * Get statistic for patterns by ids.
     * @param patternIds Patterns ids
     * @return Statistic for patterns with status.
     */
    GetStataPtrnsRsDto getStatisticForPatterns(IdsDto patternIds);

    /**
     * Merge statistic for the patterns if they are included in several
     * database documents by summation of values.
     * @param patternIds Patterns ids
     * @return Merged statistic for patterns with status.
     */
    GetStataMergedPtrnsRsDto getStatisticMergedForPatterns(IdsDto patternIds);

    /**
     * Get statistic for the document. It can be included in
     * many entries in the database.
     * @param docIdDto Document id
     * @return Get statistic for the specified document with status.
     */
    GetStataDocRsDto getStatisticForDoc(DocIdDto docIdDto);

    /**
     * Merge statistic for the document if it is included in several
     * database entries by summation of values for patterns.
     * @param docDto Document id
     * @return Merged statistic for the document with status.
     */
    GetStataMergedDocRsDto getStatisticUsageMergedForDoc(DocIdDto docDto);

    /**
     * Get download count for the documents in mapping.
     * @param docIds Documents ids
     * @return Download count for the documents in mapping (e.g.{"docId_1":5})
     *  with status.
     */
    GetDownloadDocsRsDto getDownloadsCountForDocs(IdsDto docIds);
}
