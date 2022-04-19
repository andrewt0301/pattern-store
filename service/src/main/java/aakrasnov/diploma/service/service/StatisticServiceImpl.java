package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.StatisticDto;
import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.dto.stata.AddStataRsDto;
import aakrasnov.diploma.service.dto.stata.GetDownloadDocsRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataDocRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataMergedDocRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataMergedPtrnsRsDto;
import aakrasnov.diploma.service.dto.stata.GetStataPtrnsRsDto;
import aakrasnov.diploma.service.repo.stata.StatisticDocRepo;
import aakrasnov.diploma.service.repo.stata.StatisticPtrnsRepo;
import aakrasnov.diploma.service.service.api.StatisticService;
import aakrasnov.diploma.service.utils.MergeUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

public class StatisticServiceImpl implements StatisticService {
    private final StatisticPtrnsRepo stataPtrnsRepo;

    private final StatisticDocRepo stataDocRepo;

    public StatisticServiceImpl(
        final StatisticPtrnsRepo stataPtrnsRepo,
        final StatisticDocRepo stataDocRepo
    ) {
        this.stataPtrnsRepo = stataPtrnsRepo;
        this.stataDocRepo = stataDocRepo;
    }

    @Override
    public AddStataRsDto addStatistic(final List<StatisticDto> statistics) {
        List<StatisticDoc> docs = statistics.stream().map(
            StatisticDoc::fromDto
        ).collect(Collectors.toList());
        AddStataRsDto res = new AddStataRsDto();
        docs.forEach(
            doc -> stataPtrnsRepo.save(doc.getStataPtrns())
        );
        res.setStatisticDocs(stataDocRepo.saveAll(docs));
        res.setStatus(HttpStatus.CREATED);
        return res;
    }

    @Override
    public GetStataPtrnsRsDto getStatisticForPatterns(final Set<String> patternIds) {
        GetStataPtrnsRsDto res = new GetStataPtrnsRsDto();
        res.setPtrnsStatas(stataPtrnsRepo.getStatisticForPtrns(patternIds));
        res.setStatus(HttpStatus.OK);
        return res;
    }

    @Override
    public GetStataMergedPtrnsRsDto getStatisticMergedForPatterns(final Set<String> patternIds) {
        GetStataPtrnsRsDto stataPtrns = getStatisticForPatterns(patternIds);
        if (!stataPtrns.getStatus().is2xxSuccessful()) {
            GetStataMergedPtrnsRsDto res = new GetStataMergedPtrnsRsDto();
            res.setStatus(stataPtrns.getStatus());
            res.setMsg(stataPtrns.getMsg());
            return res;
        }
        return mergePtrnsStata(stataPtrns.getPtrnsStatas());
    }

    @Override
    public GetStataDocRsDto getStatisticForDoc(final String docId) {
        GetStataDocRsDto res = new GetStataDocRsDto();
        res.setDocStatas(stataDocRepo.getStatisticForDoc(docId));
        res.setStatus(HttpStatus.OK);
        return res;
    }

    @Override
    public GetStataMergedDocRsDto getStatisticUsageMergedForDoc(final String docId) {
        GetStataDocRsDto stataDoc = getStatisticForDoc(docId);
        if (!stataDoc.getStatus().is2xxSuccessful()) {
            GetStataMergedDocRsDto res = new GetStataMergedDocRsDto();
            res.setDocId(docId);
            res.setStatus(stataDoc.getStatus());
            res.setMsg(stataDoc.getMsg());
            return res;
        }
        GetStataMergedDocRsDto res = mergeDocStata(stataDoc.getDocStatas());
        res.setDocId(docId);
        return res;
    }

    @Override
    public GetDownloadDocsRsDto getDownloadsCountForDocs(final Set<String> docIds) {
        GetDownloadDocsRsDto res = new GetDownloadDocsRsDto();
        Map<String, Integer> downloads = new HashMap<>();
        stataDocRepo.getDownloadsCountForDocs(docIds)
            .forEach(
                doc -> downloads.put(doc.get_id(), doc.getCount())
            );
        res.setDocsDownloads(downloads);
        res.setStatus(HttpStatus.OK);
        return res;
    }

    private static GetStataMergedDocRsDto mergeDocStata(final List<StatisticDoc> docStatas) {
        GetStataMergedDocRsDto res = new GetStataMergedDocRsDto();
        GetStataMergedPtrnsRsDto mergedPtrns = mergePtrnsStata(
            docStatas.stream().map(StatisticDoc::getStataPtrns).collect(Collectors.toList())
        );
        res.setSuccess(mergedPtrns.getSuccess());
        res.setFailure(mergedPtrns.getFailure());
        res.setDownload(mergedPtrns.getDownload());
        res.setStatus(mergedPtrns.getStatus());
        return res;
    }

    private static GetStataMergedPtrnsRsDto mergePtrnsStata(
        final List<StatisticPtrns> ptrnsStatas
    ) {
        GetStataMergedPtrnsRsDto res = new GetStataMergedPtrnsRsDto();
        Map<String, Integer> success = new HashMap<>();
        Map<String, Integer> failure = new HashMap<>();
        Map<String, Integer> download = new HashMap<>();
        ptrnsStatas.forEach(
            stata -> {
                new MergeUsage(stata.getSuccess()).mergeWith(success);
                new MergeUsage(stata.getFailure()).mergeWith(failure);
                new MergeUsage(stata.getDownload()).mergeWith(download);
            }
        );
        res.setSuccess(success);
        res.setFailure(failure);
        res.setDownload(download);
        res.setStatus(HttpStatus.OK);
        return res;
    }
}
