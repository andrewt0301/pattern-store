package aakrasnov.diploma.service.service;

import aakrasnov.diploma.common.feedback.FeedbackDto;
import aakrasnov.diploma.common.feedback.GetFeedbacksRsDto;
import aakrasnov.diploma.service.domain.Feedback;
import aakrasnov.diploma.service.dto.AddFeedbackRsDto;
import aakrasnov.diploma.service.repo.FeedbackRepo;
import aakrasnov.diploma.service.service.api.FeedbackService;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;

public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepo feedbackRepo;

    public FeedbackServiceImpl(final FeedbackRepo feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public AddFeedbackRsDto addFeedback(final FeedbackDto feedbackDto) {
        Feedback saved = feedbackRepo.save(Feedback.fromDto(feedbackDto));
        AddFeedbackRsDto res = new AddFeedbackRsDto();
        res.setFeedbackDto(Feedback.toDto(saved));
        res.setStatus(HttpStatus.OK.value());
        return res;
    }

    @Override
    public GetFeedbacksRsDto getFeedbacksByDocId(final String docId) {
        List<FeedbackDto> feedbacks = feedbackRepo
            .findFeedbackByDocId(new ObjectId(docId))
            .stream().map(Feedback::toDto)
            .collect(Collectors.toList());
        GetFeedbacksRsDto rs = new GetFeedbacksRsDto();
        rs.setStatus(HttpStatus.OK.value());
        rs.setFeedbackDtos(feedbacks);
        return rs;
    }
}
