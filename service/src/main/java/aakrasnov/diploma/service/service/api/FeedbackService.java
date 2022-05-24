package aakrasnov.diploma.service.service.api;

import aakrasnov.diploma.common.feedback.FeedbackDto;
import aakrasnov.diploma.common.feedback.GetFeedbacksRsDto;
import aakrasnov.diploma.service.dto.AddFeedbackRsDto;

public interface FeedbackService {
    /**
     * Add feedback from user to the DB.
     * @param feedbackDto Entity with feedback
     * @return Added feedback with status.
     */
    AddFeedbackRsDto addFeedback(FeedbackDto feedbackDto);

    /**
     * Get feedbacks from DB by the specified document id.
     * @param docId Document id
     * @return Feedback about document.
     */
    GetFeedbacksRsDto getFeedbacksByDocId(String docId);
}
