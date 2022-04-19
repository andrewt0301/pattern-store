package aakrasnov.diploma.client;

import aakrasnov.diploma.common.StatisticDto;
import java.util.List;

public interface ClientStatisticApi {
    void sendDocsStatistic(List<StatisticDto> statistics);
}
