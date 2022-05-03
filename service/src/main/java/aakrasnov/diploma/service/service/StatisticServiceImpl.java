package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.stata.AddStataRsDto;
import aakrasnov.diploma.common.stata.GetDownloadDocsRsDto;
import aakrasnov.diploma.common.stata.GetStataDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedDocRsDto;
import aakrasnov.diploma.common.stata.GetStataMergedPtrnsRsDto;
import aakrasnov.diploma.common.stata.GetStataPtrnsRsDto;
import aakrasnov.diploma.common.stata.StatisticDocDto;
import aakrasnov.diploma.common.stata.StatisticDto;
import aakrasnov.diploma.common.stata.StatisticPtrnsDto;
import aakrasnov.diploma.service.domain.StatisticDoc;
import aakrasnov.diploma.service.domain.StatisticPtrns;
import aakrasnov.diploma.service.repo.stata.StatisticDocRepo;
import aakrasnov.diploma.service.repo.stata.StatisticPtrnsRepo;
import aakrasnov.diploma.service.service.api.StatisticService;
import aakrasnov.diploma.service.utils.MergeUsage;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
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
            doc -> {
                StatisticPtrns stataPtrns = stataPtrnsRepo.save(doc.getStataPtrns());
                doc.getStataPtrns().setId(stataPtrns.getId());
            }
        );
        res.setStatisticDocs(
            stataDocRepo.saveAll(docs).stream()
                .map(StatisticDoc::toDto)
                .collect(Collectors.toList())
        );
        res.setStatus(HttpStatus.CREATED.value());
        return res;
    }

    @Override
    public GetStataPtrnsRsDto getStatisticForPatterns(final Set<String> patternIds) {
        GetStataPtrnsRsDto res = new GetStataPtrnsRsDto();
        res.setPtrnsStatas(
            stataPtrnsRepo.getStatisticForPtrns(toObjectIds(patternIds))
                .stream().map(StatisticPtrns::toDto)
                .collect(Collectors.toList())
        );
        res.setStatus(HttpStatus.OK.value());
        return res;
    }

    @Override
    public GetStataMergedPtrnsRsDto getStatisticMergedForPatterns(final Set<String> patternIds) {
        GetStataPtrnsRsDto stataPtrns = getStatisticForPatterns(patternIds);
        if (!HttpStatus.valueOf(stataPtrns.getStatus()).is2xxSuccessful()) {
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
        res.setDocStatas(
            stataDocRepo.getStatisticForDoc(new ObjectId(docId))
                .stream().map(StatisticDoc::toDocDto)
                .collect(Collectors.toList())
        );
        res.setStatus(HttpStatus.OK.value());
        return res;
    }

    @Override
    public GetStataMergedDocRsDto getStatisticUsageMergedForDoc(final String docId) {
        GetStataDocRsDto stataDoc = getStatisticForDoc(docId);
        if (!HttpStatus.valueOf(stataDoc.getStatus()).is2xxSuccessful()) {
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
        stataDocRepo.getDownloadsCountForDocs(toObjectIds(docIds))
            .forEach(
                doc -> downloads.put(doc.get_id(), doc.getCount())
            );
        res.setDocsDownloads(downloads);
        res.setStatus(HttpStatus.OK.value());
        return res;
    }

    private static GetStataMergedDocRsDto mergeDocStata(final List<StatisticDocDto> docStatas) {
        GetStataMergedDocRsDto res = new GetStataMergedDocRsDto();
        GetStataMergedPtrnsRsDto mergedPtrns = mergePtrnsStata(
            docStatas.stream().map(StatisticDocDto::getStataPtrns).collect(Collectors.toList())
        );
        res.setSuccess(mergedPtrns.getSuccess());
        res.setFailure(mergedPtrns.getFailure());
        res.setDownload(mergedPtrns.getDownload());
        res.setStatus(mergedPtrns.getStatus());
        return res;
    }

    private static GetStataMergedPtrnsRsDto mergePtrnsStata(
        final List<StatisticPtrnsDto> ptrnsStatas
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
        res.setStatus(HttpStatus.OK.value());
        return res;
    }

    private Set<ObjectId> toObjectIds(Collection<String> ids) {
        return ids.stream().map(ObjectId::new)
            .collect(Collectors.toSet());
    }
}
