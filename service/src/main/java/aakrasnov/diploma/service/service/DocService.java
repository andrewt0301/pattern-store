package aakrasnov.diploma.service.service;

import aakrasnov.diploma.service.domain.Doc;
import aakrasnov.diploma.service.repo.DocRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocService {
    private final DocRepo docRepo;

    @Autowired
    public DocService(final DocRepo docRepo) {
        this.docRepo = docRepo;
    }

    public Doc save(final Doc doc) {
        return docRepo.save(doc);
    }

    public Optional<Doc> getById(final String id) {
        return docRepo.findById(id);
    }

    public List<Doc> findAll() {
        return docRepo.findAll();
    }
}
