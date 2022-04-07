package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.Doc;
import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.Team;
import aakrasnov.diploma.service.repo.DocRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.api.DocService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DocServiceImpl implements DocService {
    private final DocRepo docRepo;

    private final UserRepo userRepo;

    @Autowired
    public DocServiceImpl(final DocRepo docRepo, final UserRepo userRepo) {
        this.docRepo = docRepo;
        this.userRepo = userRepo;
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
    public HttpStatus update(final String id, final DocDto updDto, final String userId) {
        Optional<Doc> fromDb = docRepo.findById(id);
        if (!fromDb.isPresent()) {
            return HttpStatus.BAD_REQUEST;
        }
        boolean isInTeam = fromDb.get().getTeam().equals(Team.fromDto(updDto.getTeam()));
        boolean isAdmin = userRepo.findById(userId)
            .map(user -> user.getRole().equals(Role.ADMIN))
            .orElse(false);
        if (!isInTeam && !isAdmin) {
            return HttpStatus.FORBIDDEN;
        }
        updDto.setId(id);
        Doc saved = docRepo.save(Doc.fromDto(updDto));
        updDto.setId(saved.getId());
        return HttpStatus.OK;
    }

    @Override
    public List<DocDto> filteredDocuments(final List<Filter> filters) {
        return docRepo.filteredDocuments(filters)
            .stream().map(Doc::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<DocDto> getAllDocs() {
        return docRepo.findAll()
            .stream().map(Doc::toDto)
            .collect(Collectors.toList());
    }

}
