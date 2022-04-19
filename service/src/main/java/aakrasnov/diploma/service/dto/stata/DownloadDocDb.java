package aakrasnov.diploma.service.dto.stata;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadDocDb implements Serializable {
    private String _id;
    private Integer count;
}
