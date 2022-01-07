package aakrasnov.diploma.service.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString(of = {"id", "login"})
@Document("users")
public class User {
    @Id
    private String id;
    private String login;
    private String password;
    private Role role;
    private boolean isActive;
}
