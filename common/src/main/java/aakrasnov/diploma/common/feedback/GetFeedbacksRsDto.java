package aakrasnov.diploma.common.feedback;

import aakrasnov.diploma.common.RsBaseDto;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetFeedbacksRsDto extends RsBaseDto implements Serializable {
    private List<FeedbackDto> feedbackDtos;
}
