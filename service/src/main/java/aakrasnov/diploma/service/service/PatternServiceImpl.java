package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.PatternDto;
import aakrasnov.diploma.service.domain.Pattern;
import aakrasnov.diploma.service.repo.PatternRepo;
import aakrasnov.diploma.service.service.api.PatternService;
import java.util.Optional;

public class PatternServiceImpl implements PatternService {
    private final PatternRepo ptrnRepo;

    public PatternServiceImpl(final PatternRepo ptrnRepo) {
        this.ptrnRepo = ptrnRepo;
    }

    @Override
    public Optional<PatternDto> getById(final String id) {
        return ptrnRepo.findById(id).map(Pattern::toDto);
    }

    @Override
    public PatternDto addPattern(final PatternDto pattern) {
        return Pattern.toDto(
            ptrnRepo.save(Pattern.fromDto(pattern))
        );
    }

//    public List<Pattern> findByAuthor(final User author) {
//        return ptrnRepo.findPatternByAuthorId(author.getId());
//    }
}
