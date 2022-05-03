package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.common.stata.AddStataRsDto;
import aakrasnov.diploma.common.stata.GetDownloadDocsRsDto;
import aakrasnov.diploma.common.stata.GetStataDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedPtrnsRsDto;
import aakrasnov.diploma.common.stata.GetStataPtrnsRsDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.domain.Usage;
import java.util.List;
import java.util.Set;

public interface StatisticService {
    /**
     * Save information from the passed collection with statistic.
     * It saves this information to the document with statistic for
     * documents {@link StatisticDoc} and to the separate document with
     * statistic for patterns {@link StatisticPtrns}.
     * @param statistics Collection with statistic about documents
     *  and their patterns
     * @return Saved statistic with status.
     */
    AddStataRsDto addStatistic(List<StatisticDto> statistics);

    /**
     * Get statistic about usage for the passed ids of the patterns.
     * It does not merge this information because {@link Usage} may
     * contain some metadata which could not be merged.
     * @param patternIds Pattern ids
     * @return Collection with statistic for patterns with status.
     */
    GetStataPtrnsRsDto getStatisticForPatterns(Set<String> patternIds);

    /**
     * Get merged statistic about usage for the passed ids of the patterns.
     * Statistic about each pattern stores only number of usage.
     * @param patternIds Pattern ids
     * @return Collection with statistic for patterns with status.
     */
    GetStataMergedPtrnsRsDto getStatisticMergedForPatterns(Set<String> patternIds);

    /**
     * Get collection of statistics about usage of patterns for the specified
     * document. It does not merge this information because {@link Usage}
     * may contain some metadata which could not be merged.
     * @param docId Document id
     * @return Collection with statistics about usage of patterns
     *  for the specified document with status.
     */
    GetStataDocRsDto getStatisticForDoc(String docId);

    /**
     * Merge information about usage of patterns for each of the statistic
     * (e.g. success, failure, download) for the document.
     * Statistic about each pattern stores only number of usage.
     * @param docId Document id
     * @return Merged statistic about usage of patterns
     *  for the specified document with status.
     */
    GetStataMergedDocRsDto getStatisticUsageMergedForDoc(String docId);

    /**
     * Get the number of downloads for the documents.
     * @param docIds Document ids
     * @return The number of document downloads.
     */
    GetDownloadDocsRsDto getDownloadsCountForDocs(Set<String> docIds);
}
