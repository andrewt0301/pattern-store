package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.Doc;
import aakrasnov.diploma.service.repo.DocRepo;
import aakrasnov.diploma.service.service.api.DocService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocServiceImpl implements DocService {
    private final DocRepo docRepo;

    @Autowired
    public DocServiceImpl(final DocRepo docRepo) {
        this.docRepo = docRepo;
    }

    @Override
    public Optional<DocDto> findById(final String id) {
        return docRepo.findById(id)
            .map(Doc::toDto);
    }

    @Override
    public DocDto addDoc(final DocDto docDto) {
        return Doc.toDto(
            docRepo.save(Doc.fromDto(docDto))
        );
    }

    @Override
    public void deleteById(final String id) {
        docRepo.deleteById(id);
    }

    @Override
    public DocDto update(final String id, final DocDto updDto) {
        updDto.setId(id);
        return Doc.toDto(
            docRepo.save(Doc.fromDto(updDto))
        );
    }

    @Override
    public List<DocDto> filteredDocuments(final Filter filter) {
        return docRepo.filteredDocuments(filter)
            .stream().map(Doc::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<DocDto> filteredDocuments(final List<Filter> filters) {
        return docRepo.filteredDocuments(filters)
            .stream().map(Doc::toDto)
            .collect(Collectors.toList());
    }

}
