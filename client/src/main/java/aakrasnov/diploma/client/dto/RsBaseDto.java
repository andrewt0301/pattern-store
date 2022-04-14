package aakrasnov.diploma.client.dto;

import lombok.Data;
import org.apache.http.HttpStatus;

@Data
public class RsBaseDto {
    private HttpStatus status;
    private String msg;
}
