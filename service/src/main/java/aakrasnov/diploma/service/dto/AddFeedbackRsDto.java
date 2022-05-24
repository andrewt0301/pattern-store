package aakrasnov.diploma.service.dto;

import aakrasnov.diploma.common.feedback.FeedbackDto;
import aakrasnov.diploma.common.RsBaseDto;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddFeedbackRsDto extends RsBaseDto implements Serializable {
    private FeedbackDto feedbackDto;
}
