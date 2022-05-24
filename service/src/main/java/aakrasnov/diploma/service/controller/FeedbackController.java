package aakrasnov.diploma.service.controller;

import aakrasnov.diploma.common.feedback.FeedbackDto;
import aakrasnov.diploma.common.feedback.GetFeedbacksRsDto;
import aakrasnov.diploma.service.dto.AddFeedbackRsDto;
import aakrasnov.diploma.service.service.api.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${server.api}")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(final FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("feedback/add")
    public ResponseEntity<AddFeedbackRsDto> addFeedback(
        @RequestBody FeedbackDto feedbackDto
    ) {
        AddFeedbackRsDto rs = feedbackService.addFeedback(feedbackDto);
        return new ResponseEntity<>(rs, HttpStatus.valueOf(rs.getStatus()));
    }

    @GetMapping("feedback/doc/{docId}")
    public ResponseEntity<GetFeedbacksRsDto> getFeedbacksByDocId(
        @PathVariable(name = "docId") String docId
    ) {
        GetFeedbacksRsDto rs = feedbackService.getFeedbacksByDocId(docId);
        return new ResponseEntity<>(rs, HttpStatus.valueOf(rs.getStatus()));
    }

}
