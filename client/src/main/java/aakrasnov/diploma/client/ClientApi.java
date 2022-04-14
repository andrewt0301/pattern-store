package aakrasnov.diploma.client;

import aakrasnov.diploma.client.domain.User;
import aakrasnov.diploma.client.dto.RsBaseDto;
import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ClientApi {
    /**
     * Get document from common pool by id.
     * This method is available without authentication.
     * @param id Document id
     * @return Found document in case of existence.
     * @throws IOException In case of IO exception.
     */
    Optional<DocDto> getDocFromCommon(String id) throws IOException;

    /**
     * Apply passed filters to the documents from common pool.
     * @param filters Filters for applying
     * @return Filtered documents from common pool.
     */
    List<DocDto> filterDocsFromCommon(List<Filter> filters);

    /**
     * Get document by the specified id using passed user.
     * User should be authenticated and have access to the document
     * in order to get documents.
     * @param id Document id
     * @param user User identity
     * @return Document
     */
    Optional<DocDto> getDoc(String id, User user);

    /**
     * Delete document by the specified id.
     * @param id Document id
     * @param user User identity
     */
    void deleteById(String id, User user);

    /**
     * Add a new document.
     * @param document Document for addition
     * @param user User identity
     */
    void add(DocDto document, User user);

    /**
     * Update existed document by the passed document for the specified id.
     * @param id Document id for update
     * @param docUpd Document with info which will be saved
     * @param user User identity
     * @return Updated document.
     */
    RsBaseDto update(String id, DocDto docUpd, User user);

    /**
     * Apply passed filters to the documents.
     * @param filters Filters for applying
     * @param user User identity
     * @return Filtered documents.
     */
    List<DocDto> filterDocuments(List<Filter> filters, User user);
}
