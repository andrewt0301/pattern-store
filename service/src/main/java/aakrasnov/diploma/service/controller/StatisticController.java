package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.service.dto.stata.AddStataRsDto;
import aakrasnov.diploma.service.dto.stata.DocIdDto;
import aakrasnov.diploma.service.dto.stata.DocIdsDto;
import aakrasnov.diploma.service.dto.stata.GetDownloadDocsRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataDocRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataMergedDocRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataMergedPtrnsRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataPtrnsRsDto;
import aakrasnov.diploma.service.dto.stata.PtrnIdsDto;
import aakrasnov.diploma.service.service.api.StatisticService;
import aakrasnov.diploma.service.utils.MyTmpTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        @RequestBody AddStataRsDto addStata
    ) {
        AddStataRsDto res = statisticService.addStatistic(addStata.getStatisticDocs());
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PostMapping("patterns/usage")
    public ResponseEntity<GetStataPtrnsRsDto> getStatisticForPatterns(
        @RequestBody PtrnIdsDto patternIds
    ) {
        GetStataPtrnsRsDto res = statisticService.getStatisticForPatterns(patternIds.getPtrnIds());
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PostMapping("patterns/usage/merged")
    public ResponseEntity<GetStataMergedPtrnsRsDto> getStatisticMergedForPatterns(
        @RequestBody PtrnIdsDto patternIds
    ) {
        GetStataMergedPtrnsRsDto res = statisticService.getStatisticMergedForPatterns(
            patternIds.getPtrnIds()
        );
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PostMapping("doc/usage")
    public ResponseEntity<GetStataDocRsDto> getStatisticForDoc(
        @RequestBody DocIdDto docId
    ) {
        GetStataDocRsDto res = statisticService.getStatisticForDoc(docId.getDocId());
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PostMapping("doc/usage/merged")
    public ResponseEntity<GetStataMergedDocRsDto> getStatisticUsageMergedForDoc(
        @RequestBody DocIdDto docId
    ) {
        GetStataMergedDocRsDto res = statisticService.getStatisticUsageMergedForDoc(
            docId.getDocId()
        );
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PostMapping("docs/downloads/count")
    public ResponseEntity<GetDownloadDocsRsDto> getDownloadsCountForDocs(
        @RequestBody DocIdsDto docIds
    ) {
        GetDownloadDocsRsDto res = statisticService.getDownloadsCountForDocs(docIds.getDocIds());
        return new ResponseEntity<>(res, res.getStatus());
    }
}
