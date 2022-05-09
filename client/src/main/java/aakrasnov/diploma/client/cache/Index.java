package aakrasnov.diploma.client.cache;

import aakrasnov.diploma.common.DocDto;
import java.util.List;
import java.util.Optional;

public interface Index {
    /**
     * Add information about passed documents to the index file.
     * If these docs already exist in the index file, it just
     * overwrites them.
     * @param docDtos Docs for caching
     */
    void cacheDocs(List<DocDto> docDtos);

    /**
     * Invalidate information about document in the index file.
     * Also it deletes file with cached document.
     * @param docId Document id
     */
    void invalidateByDocId(String docId);

    /**
     * Get info about cached document if it is present in the index file.
     * @param docId Document id
     * @return Info about document from index file, if this document has been cached,
     *  empty otherwise.
     */
    Optional<CachedDocInfo> getCachedDocInfo(String docId);

    /**
     * Get prefix of the path.
     * @return Prefix path.
     */
    String getPrefix();
}
