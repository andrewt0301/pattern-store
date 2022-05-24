package aakrasnov.diploma.common.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"id", "text"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDto implements Serializable {
    private String id;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("docId")
    private String docId;

    @JsonProperty("text")
    private String text;
}
