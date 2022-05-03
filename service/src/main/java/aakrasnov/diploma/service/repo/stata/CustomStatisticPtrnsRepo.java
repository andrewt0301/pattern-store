package aakrasnov.diploma.service.repo.stata;

import aakrasnov.diploma.service.domain.StatisticPtrns;
import java.util.List;
import java.util.Set;
import org.bson.types.ObjectId;

public interface CustomStatisticPtrnsRepo {
    List<StatisticPtrns> getStatisticForPtrns(Set<ObjectId> ptrnIds);
}
