package aakrasnov.diploma.service.repo.stata;

import aakrasnov.diploma.service.domain.StatisticPtrns;
import java.util.List;
import java.util.Set;

public interface CustomStatisticPtrnsRepo {
    List<StatisticPtrns> getStatisticForPtrns(Set<String> ptrnIds);
}
