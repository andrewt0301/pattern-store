package aakrasnov.diploma.service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString(of = {"id", "login"})
@Document("users")
@RequiredArgsConstructor
public class User {
    @Id
    private String id;
    @NonNull
    private String login;
    @NonNull
    private String password;
    @NonNull
    private Role role;
    private boolean isActive = false;
}
