package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import java.util.List;
import java.util.Optional;

public interface DocService {
    Optional<DocDto> findById(String id);

    DocDto addDoc(DocDto docDto);

    void deleteById(String id);

    DocDto update(String id, DocDto altered);

    List<DocDto> filteredDocuments(Filter filter);

    List<DocDto> filteredDocuments(List<Filter> filters);
}
