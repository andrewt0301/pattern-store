package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.Doc;
import java.util.List;

public interface CustomDocRepo {
    List<Doc> filteredDocuments(List<Filter> filters);
}
