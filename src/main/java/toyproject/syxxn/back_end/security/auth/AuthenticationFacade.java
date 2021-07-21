package toyproject.syxxn.back_end.security.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Long getUserId() {
        return Long.getLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
