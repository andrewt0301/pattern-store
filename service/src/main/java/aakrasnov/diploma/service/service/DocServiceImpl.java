package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.DocDto;
import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.Doc;
import aakrasnov.diploma.service.domain.Role;
import aakrasnov.diploma.service.domain.User;
import aakrasnov.diploma.service.dto.UpdateRsDto;
import aakrasnov.diploma.service.repo.DocRepo;
import aakrasnov.diploma.service.repo.UserRepo;
import aakrasnov.diploma.service.service.api.DocService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

public class DocServiceImpl implements DocService {
    private final DocRepo docRepo;

    private final UserRepo userRepo;

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
    public UpdateRsDto update(final String id, final DocDto updDto, final User user) {
        Optional<Doc> fromDb = docRepo.findById(id);
        UpdateRsDto rs = new UpdateRsDto();
        if (!fromDb.isPresent()) {
            rs.setStatus(HttpStatus.BAD_REQUEST);
            rs.setMsg(String.format("Doc with id '%s' was not found", id));
            return rs;
        }
        boolean isInTeam = user.getTeams().contains(fromDb.get().getTeam());
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        if (!isInTeam && !isAdmin) {
            rs.setStatus(HttpStatus.FORBIDDEN);
            rs.setMsg("Operation is forbidden. You should be in the team or an admin");
            return rs;
        }
        updDto.setId(id);
        Doc saved = docRepo.save(Doc.fromDto(updDto));
        updDto.setId(saved.getId());
        rs.setStatus(HttpStatus.OK);
        return rs;
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
