package toyproject.syxxn.back_end.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import toyproject.syxxn.back_end.exception.UserNotUnauthenticatedException;

@Component
public class AuthenticationFacade {

    public String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UserNotUnauthenticatedException();
        }
        return authentication.getName();
    }

}
