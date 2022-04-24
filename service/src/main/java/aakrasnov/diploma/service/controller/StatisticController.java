package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.stata.AddStataRsDto;
import aakrasnov.diploma.common.stata.DocIdDto;
import aakrasnov.diploma.common.stata.GetDownloadDocsRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedPtrnsRsDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import aakrasnov.diploma.service.dto.stata.GetStataDocRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataPtrnsRsDto;
import aakrasnov.diploma.service.service.api.StatisticService;
import aakrasnov.diploma.service.utils.MyTmpTest;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("${server.api}/statistic")
@RestController
public class StatisticController {
    @Autowired
    private MyTmpTest myTmpTest;

    @Autowired
    private StatisticService statisticService;

    @GetMapping
    public ResponseEntity<String> checkStatisticServiceMethods() {
        myTmpTest.call();
        return ResponseEntity.ok("");
    }

    @PostMapping
    public ResponseEntity<AddStataRsDto> addStatistic(
        @RequestBody List<StatisticDto> stataDtos
    ) {
        AddStataRsDto res = statisticService.addStatistic(stataDtos);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @PostMapping("patterns/usage")
    public ResponseEntity<GetStataPtrnsRsDto> getStatisticForPatterns(
        @RequestBody Set<String> patternIds
    ) {
        GetStataPtrnsRsDto res = statisticService.getStatisticForPatterns(patternIds);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @PostMapping("patterns/usage/merged")
    public ResponseEntity<GetStataMergedPtrnsRsDto> getStatisticMergedForPatterns(
        @RequestBody Set<String> patternIds
    ) {
        GetStataMergedPtrnsRsDto res = statisticService
            .getStatisticMergedForPatterns(patternIds);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @PostMapping("doc/usage")
    public ResponseEntity<GetStataDocRsDto> getStatisticForDoc(
        @RequestBody DocIdDto docDto
    ) {
        GetStataDocRsDto res = statisticService
            .getStatisticForDoc(docDto.getDocId());
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @PostMapping("doc/usage/merged")
    public ResponseEntity<GetStataMergedDocRsDto> getStatisticUsageMergedForDoc(
        @RequestBody DocIdDto docDto
    ) {
        GetStataMergedDocRsDto res = statisticService
            .getStatisticUsageMergedForDoc(docDto.getDocId());
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @PostMapping("docs/downloads/count")
    public ResponseEntity<GetDownloadDocsRsDto> getDownloadsCountForDocs(
        @RequestBody Set<String> docIds
    ) {
        GetDownloadDocsRsDto res = statisticService.getDownloadsCountForDocs(docIds);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }
}
