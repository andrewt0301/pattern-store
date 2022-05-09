package aakrasnov.diploma.client.cache;

import aakrasnov.diploma.common.DocDto;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IndexFile {
    /**
     * Get creation time of the index file.
     * @return Time of index creation.
     */
    LocalDateTime getIndexTimeCreation();

    /**
     * Get cached document info from index file if document id exist.
     * It does not matter whether it is fresh.
     * @param docId Document id
     * @return Document info if document id exist in the index file,
     *  empty otherwise.
     */
    Optional<CachedDocInfo> getCachedDocInfo(String docId);

    /**
     * Remove info about cached document by id.
     * @param docId Document id
     * @return Return information about deleted cached document if it
     *  is present in the index file, empty otherwise.
     */
    Optional<CachedDocInfo> removeDoc(String docId);

    /**
     * Cache passed documents in the index file.
     * @param docs Documents for caching
     * @param docsPaths Paths to the files with cached documents. Each file contains
     *  information about one document.
     */
    void cacheDocs(List<DocDto> docs, List<String> docsPaths);

    /**
     * Convert index file to string.
     * @return String representation of index file.
     */
    String asString();

    /**
     * Write content of the index file to the output stream.
     * @param output Output stream for writing
     * @implNote It does not close output stream.
     */
    void writeTo(OutputStream output) throws IOException;
}
