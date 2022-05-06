package aakrasnov.diploma.client.cache;

import aakrasnov.diploma.common.DocDto;
import java.util.List;

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
     * @param docId Document id
     */
    void invalidateByDocId(String docId);

    /**
     * Check whether entry with passed document id is present and is actual
     * in the index file.
     * @param docId Document id
     * @return True - if present and current time minus caching time
     *  is less than 1 hour, otherwise - false.
     */
    boolean isDocActual(String docId);
}
