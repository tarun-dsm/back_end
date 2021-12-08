package toyproject.syxxn.back_end.service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import toyproject.syxxn.back_end.exception.UserNotAuthenticatedException;

@Component
public class AuthenticationUtil {

    public String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw UserNotAuthenticatedException.EXCEPTION;
        }
        return authentication.getName();
    }

}
