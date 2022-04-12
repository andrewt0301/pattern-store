package aakrasnov.diploma.service.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class StatusBaseDto {
    private HttpStatus status;
    private String msg;
}
