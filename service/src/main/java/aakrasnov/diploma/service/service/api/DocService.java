package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.AddDocRsDto;
import aakrasnov.diploma.service.dto.UpdateRsDto;
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
     * @return Added document and response status.
     */
    AddDocRsDto addDoc(DocDto docDto);

    /**
     * Delete document by the specified id.
     * @param id Id of the document
     */
    void deleteById(String id);

    /**
     * Update document by the specified id with passed dto.
     * For a successful update an author of the document for
     * update should be either in the team of the creators of the
     * document by passed id or an admin.
     * @param id Id of the document for update
     * @param updDto The document with updated info
     * @param user User, who performs update
     * @return Result of operation (OK, BAD_REQUEST, FORBIDDEN) with optional message.
     */
    UpdateRsDto update(String id, DocDto updDto, User user);

    /**
     * Get collection with documents which match passed filters.
     * @param filters Filters to limit documents
     * @return List of documents which match filters.
     */
    List<DocDto> filteredDocuments(List<Filter> filters);

    /**
     * Obtains all existing documents. This operation should be available
     * only for admin users.
     * @return All documents.
     */
    List<DocDto> getAllDocs();
}
