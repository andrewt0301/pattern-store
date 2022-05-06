package aakrasnov.diploma.service.utils;

import aakrasnov.diploma.service.domain.User;
import java.security.Principal;
import org.springframework.security.core.Authentication;

public class PrincipalConverter {
    private final Principal principal;

    public PrincipalConverter(final Principal principal) {
        this.principal = principal;
    }

    public User toUser() {
        Authentication authentication = (Authentication) principal;
        if (authentication == null) {
            throw new IllegalStateException("User could not be null here");
        }
        return (User) authentication.getPrincipal();
    }
}
