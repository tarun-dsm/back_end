package toyproject.syxxn.back_end.security.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
