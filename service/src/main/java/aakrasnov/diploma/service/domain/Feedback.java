package aakrasnov.diploma.service.domain;

import aakrasnov.diploma.common.feedback.FeedbackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@EqualsAndHashCode(of = {"id", "text"})
@ToString(of = {"id", "text"})
@NoArgsConstructor
@AllArgsConstructor
@Document(DocumentNames.FEEDBACK)
public class Feedback {
    @Id
    private ObjectId id;

    private ObjectId userId;

    private ObjectId docId;

    private String text;

    public static Feedback fromDto(FeedbackDto dto) {
        Feedback feedback = new Feedback();
        if (dto.getId() != null) {
            feedback.setId(new ObjectId(dto.getId()));
        }
        if (dto.getUserId() != null) {
            feedback.setUserId(new ObjectId(dto.getUserId()));
        }
        if (dto.getDocId() != null) {
            feedback.setDocId(new ObjectId(dto.getDocId()));
        }
        feedback.setText(dto.getText());
        return feedback;
    }

    public static FeedbackDto toDto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        if (feedback.getId() != null) {
            dto.setId(feedback.getId().toHexString());
        }
        if (feedback.getUserId() != null) {
            dto.setUserId(feedback.getUserId().toHexString());
        }
        if (feedback.getDocId() != null) {
            dto.setDocId(feedback.getDocId().toHexString());
        }
        dto.setText(feedback.getText());
        return dto;
    }
}
