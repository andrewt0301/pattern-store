package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import java.util.List;
import java.util.Optional;

/**
 * Logic for interacting with documents.
 */
public interface DocService {
    /**
     * Find document with patterns by id.
     * @param id Id of the document
     * @return Found documents or empty in case of absence.
     */
    Optional<DocDto> findById(String id);

    /**
     * Add a new document to the database.
     * @param docDto Document which should be added
     * @return Added document.
     */
    DocDto addDoc(DocDto docDto);

    /**
     * Delete document by the specified id.
     * @param id Id of the document
     */
    void deleteById(String id);

    /**
     * Update document by the specified id with passed dto.
     * @param id Id of the document for update
     * @param updDto The document with updated info
     * @return Saved updated document.
     */
    DocDto update(String id, DocDto updDto);

    /**
     * Get collection with documents which match filter.
     * @param filter Filter to limit documents
     * @return List of documents which match filter.
     */
    List<DocDto> filteredDocuments(Filter filter);

    /**
     * Get collection with documents which match passed filters.
     * @param filters Filters to limit documents
     * @return List of documents which match filters.
     */
    List<DocDto> filteredDocuments(List<Filter> filters);
}
