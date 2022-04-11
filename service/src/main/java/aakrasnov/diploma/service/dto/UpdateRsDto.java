package aakrasnov.diploma.service.dto;

import java.io.Serializable;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UpdateRsDto implements Serializable {
    private HttpStatus status;
    private String msg;
}
